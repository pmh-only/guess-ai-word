const { fetch } = require('undici')
const prompts = require('prompts')
const chalk = require('chalk')
const cj = require('color-json')

const gameTypeValues = {
  NORMAL: {
    round: 1,
    askable: 5,
    candidate: 3,
    askThrottle: 5,
    submitThrottle: 5
  },
  SPEEDRUN: {
    round: 5,
    askable: 100,
    candidate: 3,
    askThrottle: 1,
    submitThrottle: 5
  }
}

;(async () => {

  console.log(chalk.bgCyan(' GuessAIWord '))
  console.log(chalk.gray('backend test prototype'))

  const { host } = await prompts({
    type: 'text',
    name: 'host',
    message: '백엔드 주소',
    initial: 'http://localhost:8080/api'
  })

  while (true) {
    const { menu1 } = await prompts({
      type: 'select',
      name: 'menu1',
      message: '메뉴 선택',
      choices: [
        { title: '게임 시작', value: 1 },
        { title: '게임 기록', value: 3 },
        { title: '게임 리더보드', value: 4 },
        { title: '종료', value: 2 }
      ]
    })

    if (menu1 === 2)
      break

    if (menu1 === 1) {
      const { gameType, dictionaryCategory } = await prompts([
        {
          type: 'select',
          name: 'gameType',
          message: '게임타입 선택',
          choices: [
            { title: '노말', value: 'NORMAL' },
            { title: '스피드런', value: 'SPEEDRUN' }
          ]
        },
        {
          type: 'select',
          name: 'dictionaryCategory',
          message: '단어 카테고리 선택',
          choices: [
            { title: '전체', value: 'ANY' },
            { title: '과일', value: 'FRUITS' },
            { title: '도구', value: 'TOOLS' },
            { title: '동물', value: 'ANIMALS' }
          ]
        }
      ])

      const gameToken = await fetch(`${host}/games/createNewGame`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          gameType,
          dictionaryCategory
        })
      }).then((res) => res.headers.get('Set-Cookie').split('=')[1])

      console.log(chalk.gray(`GameToken: ${gameToken}`))

      for (let roundIndex = 0;; roundIndex++) {
        const { menu2 } = await prompts({
          type: 'select',
          name: 'menu2',
          message: '메뉴 선택',
          choices: [
            { title: '다음 라운드', value: 1, disabled: gameTypeValues[gameType].round === roundIndex },
            { title: '게임을 끝내고 스코어 계산', value: 2 }
          ]
        })

        if (menu2 === 2)
          break     

        await fetch(`${host}/games/createNewRound`, {
          method: 'POST',
          headers: { 'Cookie': `GAME_TOKEN=${gameToken}` }
        })

        let askCount = 0
        let askedAt
        let submitAt

        while (true) {
          const { menu3 } = await prompts({
            type: 'select',
            name: 'menu3',
            message: '메뉴 선택',
            choices: [
              { title: '정답 제출', value: 2 },
              { title: '질문하기 (-30점)', value: 1, disabled: askCount === gameTypeValues[gameType].askable },
              { title: '초성 힌트 보기 (점수 반토막)', value: 4 },
              { title: '이번 라운드 건너뛰기', value: 3 }
            ]
          })
    
          if (menu3 === 3)
            break
    
          if (menu3 === 1) {
            if (askedAt !== undefined && (Date.now() - askedAt) / 1000 <= gameTypeValues[gameType].askThrottle) {
              console.log(chalk.red('질문은 5초마다 한번씩 할 수 있습니다'))
              continue
            }

            const { candidates, candidateSecret } = await fetch(`${host}/games/createAskCandidate`, {
              method: 'POST',
              headers: { 'Cookie': `GAME_TOKEN=${gameToken}` }
            }).then((res) => res.json())

            console.log(chalk.gray(`CandidateSecret: ${candidateSecret}`))

            askedAt = Date.now()
            
            const { candidateId } = await prompts({
              type: 'select',
              name: 'candidateId',
              message: '질문 후보 선택',
              choices: [
                ...candidates.map((candidate) => ({
                  title: `(${candidate.id}) ${candidate.askPrompt.replace('%s', '(?)')}`,
                  value: candidate.id
                })),
                { title: '취소', value: 'cancel' }
              ]
            })

            if (candidateId === 'cancel')
              continue

            askCount++
            console.log(chalk.gray('질문중... 잠시만 기다려주세요'))

            const { response } = await fetch(`${host}/games/askToAI`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json', 'Cookie': `GAME_TOKEN=${gameToken}` },
              body: JSON.stringify({
                candidateId,
                candidateSecret
              })
            }).then((res) => res.json())

            console.log(chalk.cyan('AI 응답: ') + response)
          }

          if (menu3 === 4) {
            const { chosungs } = await fetch(`${host}/games/getChosungHint`, {
              method: 'POST',
              headers: { 'Cookie': `GAME_TOKEN=${gameToken}` }
            }).then((res) => res.json())

            console.log(chalk.cyan('초성 힌트: ') + chosungs)
          }

          if (menu3 === 2) {
            if (submitAt !== undefined && (Date.now() - submitAt) / 1000 <= gameTypeValues[gameType].submitThrottle) {
              console.log(chalk.red('정답 제출은 5초마다 한번씩 할 수 있습니다'))
              continue
            }

            const { answer } = await prompts({
              type: 'text',
              name: 'answer',
              message: '정답은??',
            })

            const { correctAnswer } = await fetch(`${host}/games/submitAnswer`, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json', 'Cookie': `GAME_TOKEN=${gameToken}` },
              body: JSON.stringify({
                answer
              })
            }).then((res) => res.json())

            submitAt = Date.now()

            if (correctAnswer) {
              console.log(chalk.greenBright('정답입니다!!'))
              break
            }

            console.log(chalk.red('아닙니다...'))
          }
        }
        
        const { correctAnswer } = await fetch(`${host}/games/getCorrectAnswer`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json', 'Cookie': `GAME_TOKEN=${gameToken}` }
        }).then((res) => res.json())

        console.log(chalk.greenBright(`정답은 ${correctAnswer}(이)였습니다!`))
      }

      const { score } = await fetch(`${host}/games/calculateScore`, {
        method: 'POST',
        headers: { 'Cookie': `GAME_TOKEN=${gameToken}` }
      }).then((res) => res.json())

      console.log(chalk.yellow('최종 스코어: ') + score + '점')

      const { playerName } = await prompts({
        type: 'text',
        name: 'playerName',
        message: '당신의 이름은? (스코어보드용, 엔터를 눌러 미등록)'
      })

      if (playerName.length < 1)
        continue

      await fetch(`${host}/games/updatePlayerName`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Cookie': `GAME_TOKEN=${gameToken}` },
        body: JSON.stringify({
          playerName
        })
      })

      console.log(chalk.greenBright('등록 완료!'))
    }

    if (menu1 === 3) {
      let lastId = 0
      while (true) {
        const { games, last: isLast } = await fetch(`${host}/games/getGameHistory`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            lastId,
            count: 10
          })
        }).then((res) => res.json())

        console.log(cj(games))

        const { menu2 } = await prompts({
          type: 'select',
          name: 'menu2',
          message: '메뉴 선택',
          choices: [
            { title: '다음 10개', value: 1, disabled: isLast },
            { title: '새로고침', value: 2 },
            { title: '돌아가기', value: 3 }
          ]
        })

        if (menu2 === 3)
          break

        if (menu2 === 1)
          lastId = games[9].id

        if (menu2 === 2)
          lastId = 0
      }
    }

    if (menu1 === 4) {
      const { gameType } = await prompts({
        type: 'select',
        name: 'gameType',
        message: '게임타입 선택',
        choices: [
          { title: '노말', value: 'NORMAL' },
          { title: '스피드런', value: 'SPEEDRUN' }
        ]
      })

      while (true) {
        const { games } = await fetch(`${host}/games/getLeaderBoard`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            gameType
          })
        }).then((res) => res.json())
    
        console.log(cj(games))
  
        const { menu2 } = await prompts({
          type: 'select',
          name: 'menu2',
          message: '메뉴 선택',
          choices: [
            { title: '새로고침', value: 1 },
            { title: '돌아가기', value: 2 }
          ]
        })

        if (menu2 === 2)
          break
      }
    }
  }
})()

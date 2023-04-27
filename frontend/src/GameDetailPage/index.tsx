import { useEffect, type FC } from 'react'
import TitleBar from '../TitleBar'
import { useLocation, useNavigate } from 'react-router-dom'
import { type GameDetail } from '../datatype'

import style from './style.module.scss'

const GameDetailPage: FC = () => {
  const navigate = useNavigate()
  const location = useLocation()
  const game = location.state?.game as GameDetail

  useEffect(() => {
    if (game === undefined) {
      navigate('/')
    }
  }, [])

  const parseGameType = (gameType: string): string => ({
    NORMAL: '노말',
    SPEEDRUN: '스피드런'
  })[gameType] as string

  const parseDictionaryCategory = (dictionaryCategory: string): string => ({
    ANY: '전체',
    ANIMALS: '동물',
    FRUITS: '과일/채소',
    TOOLS: '도구'
  })[dictionaryCategory] as string

  if (game === undefined) { return <></> }

  return (
    <>
      <TitleBar isFreepass title={`게임 #${game.id}`} />
      <div className={style.gameDetail}>
        <ul>
          <li>
            <p>플레이어 이름</p>
            <p>{game.playerName ?? '익명'}</p>
          </li>
          <li>
            <p>게임 난이도</p>
            <p>{parseGameType(game.gameType)}</p>
          </li>
          <li>
            <p>단어 카테고리</p>
            <p>{parseDictionaryCategory(game.dictionaryCategory)}</p>
          </li>
          <li>
            <p>최종 스코어</p>
            <p>{game.finalScore}점</p>
          </li>
          <li>
            <ul>
              {game.rounds.map((round, i) => (
                <li key={i}>
                  <p>{i + 1}라운드</p>
                  <ul>
                    <li>
                      <p>라운드 정답</p>
                      <p>{round.answer}</p>
                    </li>
                    <li>
                      <p>라운드 결과</p>
                      <p>{round.correctAnswer ? '맞춤' : '맞추지 못함'}</p>
                    </li>
                    <li>
                      <p>초성 힌트 사용</p>
                      <p>{round.chosungHintShowed ? '사용함' : '사용하지 않음'}</p>
                    </li>
                    <li>
                      <ul>
                        {round.asks.map((ask, i) => (
                          <li key={i}>
                            <p>질문: {ask.askPrompt.replaceAll('%s', round.answer)}</p>
                            <p>답변: {ask.response.replaceAll('%s', round.answer)}</p>
                          </li>
                        ))}
                      </ul>
                    </li>
                  </ul>
                </li>
              ))}
            </ul>
          </li>
        </ul>
      </div>
    </>
  )
}

export default GameDetailPage

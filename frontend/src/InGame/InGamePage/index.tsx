import { useEffect, type FC, useState, createRef } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import TitleBar from '../../GameTitleBar'
import { motion } from 'framer-motion'

import style from './style.module.scss'
import { MdQuestionAnswer, MdSend, MdSkipNext, MdTextFields, MdTimer } from 'react-icons/md'
import FanfareParticle from './FanfareParticle'

interface QnAListItem {
  question: string
  answer: string | null
}

const InGamePage: FC = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const { state } = location

  const [qnaList, setQnaResult] = useState<QnAListItem[]>([])
  const inputRef = createRef<HTMLInputElement>()
  const [input, setInput] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [isCorrect, setIsCorrect] = useState(false)
  const [isWrong, setIsWrong] = useState(false)
  const [isFocused, setIsFocused] = useState(false)

  const skipRound = (): void => {
    if (state.gameTypeValues.round <= state.round) {
      navigate('/ingame/result', {
        replace: true
      })
      return
    }

    navigate('/ingame/roundNotice', {
      replace: true,
      state: { ...state, round: state.round as number + 1 }
    })
  }

  const submitAnswer = async (): Promise<void> => {
    if (input.length < 1) {
      inputRef.current?.focus()
      return
    }

    if (isLoading || isCorrect || isWrong) return
    setIsLoading(true)

    const qnaIndex = qnaList.length
    setQnaResult([...qnaList, {
      question: `정답은 ${input}인가요..?`,
      answer: null
    }])

    const { correctAnswer: isCorrectAnswer } = await fetch('/api/games/submitAnswer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        answer: input
      })
    }).then(async (res) => await res.json())

    setIsLoading(false)
    setQnaResult((qnaList) => {
      qnaList[qnaIndex].answer = isCorrectAnswer as boolean ? '정답입니다!' : '정답이 아닙니다...'
      return [...qnaList]
    })

    if (!(isCorrectAnswer as boolean)) {
      setIsWrong(true)
      setInput('')
      setTimeout(() => {
        setIsWrong(false)
      }, state.gameTypeValues.submitThrottle * 1000)
      return
    }

    setIsCorrect(true)
    setTimeout(() => {
      if (state.gameTypeValues.round >= state.round) {
        navigate('/ingame/result', {
          replace: true
        })
        return
      }

      navigate('/ingame/roundNotice', {
        replace: true,
        state: { ...state, round: state.round as number + 1 }
      })
    }, 3 * 1000)
  }

  const chosungHint = async (): Promise<void> => {
    const qnaIndex = qnaList.length
    setQnaResult((qnaList) => [
      ...qnaList,
      {
        question: '초성이 무엇인가요? (점수 50% 감소, 중복 적용 X)',
        answer: null
      }
    ])

    const { chosungs } = await fetch('/api/games/getChosungHint', {
      method: 'POST'
    }).then(async (res) => await res.json())

    setQnaResult((qnaList) => {
      qnaList[qnaIndex].answer = `정답의 초성은 ${chosungs as string}입니다`
      return [...qnaList]
    })
  }

  useEffect(() => {
    if (state === null) {
      navigate('/', { replace: false })
    }
  }, [])

  useEffect(() => {
    inputRef.current?.addEventListener('focus', () => {
      setIsFocused(true)
    })

    inputRef.current?.addEventListener('blur', () => {
      setIsFocused(false)
    })
  }, [inputRef.current])

  if (state === null) {
    return <></>
  }

  return (
    <div className={style.container}>
      <TitleBar title={`${state.round as number}라운드`} />

      <ul className={style.qnaList}>
        {qnaList.map((qna, i) => (
          <li key={i}>
            <p>{qna.question}</p>
            <p>{qna.answer}</p>
          </li>
        ))}
      </ul>

      <div className={style.inputBox}>
        <input
          ref={inputRef}
          enterKeyHint="done"
          type="text"
          autoFocus
          autoComplete="off"
          value={input}
          className={style.inputReal}
          onKeyDown={(e) => {
            if (e.key === 'Enter') void submitAnswer()
          }}
          onChange={(e) => {
            if (!isLoading && !isCorrect && !isWrong) {
              setInput(e.target.value.trim().replace(/\d|\w/g, ''))
            }
          }} />

        <div
          onClick={() => { inputRef.current?.focus() }}
          className={`${style.input} ${isCorrect ? style.isCorrect : ''} ${isWrong ? style.isWrong : ''}`}>
          { isWrong && <i>다음 시도까지 {state.gameTypeValues.submitThrottle}초 대기...</i>}
          {!isWrong && (
            <>{input.length > 0 ? input : <i>{isFocused ? '타이핑으로' : '여기를 눌러'} 정답 작성</i>}</>
          )}
          <button onClick={() => { void submitAnswer() }}>
            {(!isLoading && !isWrong) && <MdSend />}
            {(isLoading || isWrong) && <MdTimer />}
          </button>
        </div>
      </div>

      <nav className={style.action}>
        <motion.button
          onClick={() => { skipRound() }}
          transition={{ duration: 0.2 }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}>
          <MdSkipNext size={24} />
          스킵
        </motion.button>
        <motion.button
          transition={{ duration: 0.2 }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}>
          <MdQuestionAnswer size={24} />
          질문하기
        </motion.button>
        <motion.button
          onClick={() => { void chosungHint() }}
          transition={{ duration: 0.2 }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}>
          <MdTextFields size={24} />
          초성힌트
        </motion.button>
      </nav>

      {isCorrect && <FanfareParticle />}
    </div>
  )
}

export default InGamePage

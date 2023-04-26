import { useEffect, type FC, useState, createRef } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import TitleBar from '../../TitleBar'
import { AnimatePresence, motion } from 'framer-motion'

import style from './style.module.scss'
import { MdQuestionAnswer, MdSend, MdSkipNext, MdTextFields, MdTimer } from 'react-icons/md'
import FanfareParticle from './FanfareParticle'
import PulseLoader from 'react-spinners/PulseLoader'
import reactStringReplace from 'react-string-replace'

interface QnAListItem {
  question: string
  answer: string | null
}

interface CandidateListItem {
  askPrompt: string
  id: number
}

const InGamePage: FC = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const { state } = location

  const [qnaList, setQnaResult] = useState<QnAListItem[]>([])
  const [candidateList, setCandidateList] = useState<CandidateListItem[]>([])
  const [candidateSecret, setCandidateSecret] = useState('')
  const [candidateSelect, setCandidateSelect] = useState<number | null>(null)
  const inputRef = createRef<HTMLInputElement>()
  const [input, setInput] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [isQuestionLoading, setIsQuestionLoading] = useState(false)
  const [isQuestionFinish, setIsQuestionFinish] = useState(false)
  const [questionCount, setQuestionCount] = useState(0)
  const [isCorrect, setIsCorrect] = useState(false)
  const [isWrong, setIsWrong] = useState(false)
  const [isFocused, setIsFocused] = useState(false)
  const qnaListRef = createRef<HTMLUListElement>()

  useEffect(() => {
    qnaListRef.current?.scrollTo(0, qnaListRef.current.scrollHeight)
  }, [qnaList])

  const skipRound = async (): Promise<void> => {
    setIsLoading(true)

    const { correctAnswer } = await fetch('/api/games/getCorrectAnswer', { method: 'POST' })
      .then(async (res) => await res.json())

    setInput(correctAnswer)
    setQnaResult((qnaList) => [
      ...qnaList,
      {
        question: '정답은 무엇인가요?',
        answer: `정답은 ${correctAnswer as string}였습니다!`
      }
    ])

    setTimeout(() => {
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
    }, 1000)
  }

  const submitAnswer = async (): Promise<void> => {
    if (input.length < 1) {
      inputRef.current?.focus()
      return
    }

    if (isLoading || isCorrect || isWrong) return
    setIsLoading(true)

    inputRef.current?.blur()

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
    }, 3 * 1000)
  }

  const createCandidates = async (): Promise<void> => {
    if (isQuestionLoading || isQuestionFinish) return
    setIsQuestionLoading(true)

    const { candidates, candidateSecret, isFault } = await fetch('/api/games/createAskCandidate', {
      method: 'POST'
    }).then(async (res) => await res.json())
      .catch(() => ({ isFault: true }))

    if (candidates === undefined || isFault === true) {
      setIsQuestionLoading(false)
      setIsQuestionFinish(true)
      return
    }

    setCandidateList(candidates)
    setCandidateSecret(candidateSecret)
    setQuestionCount(questionCount + 1)

    if (questionCount + 1 >= state.gameTypeValues.askable) {
      setIsQuestionFinish(true)
    }

    setTimeout(() => {
      setIsQuestionLoading(false)
    }, state.gameTypeValues.askThrottle * 1000)
  }

  const resolveCandidate = async (): Promise<void> => {
    const qnaIndex = qnaList.length
    const candidate = candidateList.find((e) => e.id === candidateSelect) as CandidateListItem

    setQnaResult([
      ...qnaList,
      {
        question: candidate.askPrompt,
        answer: null
      }
    ])

    setCandidateList([])
    setCandidateSelect(null)
    setCandidateSecret('')

    const { response } = await fetch('/api/games/askToAI', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        candidateId: candidate.id,
        candidateSecret
      })
    }).then(async (res) => await res.json())
      .catch(() => ({ response: '오류!' }))

    setQnaResult((qnaList) => {
      qnaList[qnaIndex].answer = response
      return [...qnaList]
    })
  }

  const chosungHint = async (): Promise<void> => {
    const qnaIndex = qnaList.length
    setQnaResult((qnaList) => [
      ...qnaList,
      {
        question: '초성이 무엇인가요?',
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

      <ul className={style.qnaList} ref={qnaListRef}>
        {qnaList.map((qna, i) => (
          <li key={i}>
            <p>Q. {reactStringReplace(qna.question, '%s', () =>
              <i>{input}</i>)}</p>
            {qna.answer === null &&
              <motion.p
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                className={style.loading}>
                  <PulseLoader speedMultiplier={0.5} size={10} color="#ec8b8b" />
              </motion.p>}
            {qna.answer !== null &&
              <motion.p
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}>
                {reactStringReplace(qna.answer, '%s', () =>
                <i>{input}</i>)}
              </motion.p>}
          </li>
        ))}
        <li></li>
      </ul>

      <div className={style.inputBox}>
        <input
          ref={inputRef}
          enterKeyHint="done"
          type="search"
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
          onClick={() => { void skipRound() }}
          transition={{ duration: 0.2 }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}>
          <MdSkipNext size={24} />
          스킵
          <p>0점 처리</p>
        </motion.button>
        <motion.button
          onClick={() => { void chosungHint() }}
          transition={{ duration: 0.2 }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}>
          <MdTextFields size={24} />
          초성힌트
          <p>-50%점</p>
        </motion.button>
        <motion.button
          className={isQuestionFinish ? style.finished : ''}
          onClick={() => { void createCandidates() }}
          transition={{ duration: 0.2 }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}>
          {isQuestionFinish
            ? <>남은 힌트 없음</>
            : (<>
              {isQuestionLoading ? <MdTimer size={24} /> : <MdQuestionAnswer size={24} />}
              {isQuestionLoading ? `${state.gameTypeValues.askThrottle as string}초 대기` : <>질문하기<p>-10점</p></>}
            </>)}
            <AnimatePresence>
              {qnaList.length < 1 && (
                <motion.img
                  exit={{ opacity: 0 }}
                  width={150} src="/intro.png" className={style.intro} />
              )}
            </AnimatePresence>
        </motion.button>
      </nav>

      {isCorrect && <FanfareParticle />}
      <AnimatePresence>
        {candidateList.length > 0 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className={style.candidateModal}>
            <div className={style.content}>
              <ul>
                {candidateList.map((candidate, i) => (
                  <motion.li
                    transition={{ delay: i / 10 }}
                    initial={{ opacity: 0, translateX: 100 }}
                    animate={{ opacity: 1, translateX: 0 }}
                    key={i}>
                    <button
                      onClick={() => { setCandidateSelect(candidate.id) }}
                      className={candidateSelect === candidate.id ? style.selected : ''}>
                      {candidate.askPrompt.replaceAll('%s', '???')}
                    </button>
                  </motion.li>
                ))}
              </ul>

              {candidateSelect !== null && (
                <motion.button
                  initial={{ opacity: 0, translateY: 100 }}
                  animate={{ opacity: 1, translateY: 0 }}
                  onClick={() => { void (!isLoading && resolveCandidate()) }}>
                  질문하기!
                  <MdSend />
                </motion.button>
              )}
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  )
}

export default InGamePage

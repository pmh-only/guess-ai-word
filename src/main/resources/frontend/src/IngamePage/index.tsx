import { useEffect, useState, type FC } from 'react'
import { useLocation, useNavigate, type RouteObject } from 'react-router-dom'

interface AIResult {
  question: string
  response: string
}

const IngamePage: FC = () => {
  const location = useLocation()
  const navigate = useNavigate()
  const [aiResults, setAIResults] = useState<AIResult[]>([])
  const [isDisabled, setIsDisabled] = useState(false)
  const [currentRoundIndex, setCurrentRoundIndex] = useState(1)
  const [currentAIResultIndex, setCurrentAIResultIndex] = useState(1)
  const [answer, setAnswer] = useState('???')
  const [message, setMessage] = useState('')

  const { roundCount, aiResultCount, aiResponse } = location.state.gameData

  const onNextAIResponse = (): void => {
    setIsDisabled(true)
    if (currentAIResultIndex >= aiResultCount) {
      setMessage('힌트를 모두 사용하셨습니다.')
      setIsDisabled(false)
      return
    }

    void fetch('/api/games/nextAIResponse', { method: 'POST' })
      .then(async (res) => await res.json())
      .then((res) => {
        if (res.aiResponse === undefined) {
          setMessage('힌트는 5초 마다 볼 수 있습니다.')
          setIsDisabled(false)
          return
        }

        setCurrentAIResultIndex(currentAIResultIndex + 1)
        setAIResults([
          ...aiResults,
          res.aiResponse
        ])
        setIsDisabled(false)
      })
  }

  const onSkip = (): void => {
    setIsDisabled(true)
    if (currentRoundIndex >= roundCount) {
      navigate('/score')
      return
    }

    void fetch('/api/games/nextRound', { method: 'POST' })
      .then(async (res) => await res.json())
      .then((res) => {
        setCurrentAIResultIndex(1)
        setCurrentRoundIndex(currentRoundIndex + 1)
        setAIResults([
          res.aiResponse
        ])
        setIsDisabled(false)
      })
  }

  const onSubmitAnswer = (): void => {
    setIsDisabled(true)

    void fetch('/api/games/submitAnswer', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        answer
      })
    }).then(async (res) => await res.json())
      .then((res) => {
        if (res.correct as boolean) {
          setCurrentAIResultIndex(1)
          setCurrentRoundIndex(currentRoundIndex + 1)
          setIsDisabled(false)
          setAnswer('???')
          setMessage('정답입니다')
          onSkip()
          return
        }

        setIsDisabled(false)
        setMessage('오답입니다')
      })
  }

  useEffect(() => {
    setAIResults([
      ...aiResults,
      aiResponse
    ])
  }, [])

  return (
    <div>
      {aiResults.map((aiResult, i) => (
        <div key={i}>
          {aiResult.question.replaceAll('{{}}', answer)} - {aiResult.response.replaceAll('{{}}', answer)}
        </div>
      ))}

      <input value={answer === '???' ? '' : answer} type="text" autoFocus onChange={(e) => { setAnswer(e.target.value === '' ? '???' : e.target.value) }} />
      <button disabled={isDisabled} onClick={onSkip}>스킵</button>
      <button disabled={isDisabled} onClick={onNextAIResponse}>힌트 더보기</button>
      <button disabled={isDisabled} onClick={onSubmitAnswer}>정답 제출</button>

      {message}
    </div>
  )
}

export default IngamePage
export const ingameRouter: RouteObject = {
  path: '/ingame',
  element: <IngamePage />
}

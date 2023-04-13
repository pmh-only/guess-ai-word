import { useEffect, useState, type FC } from 'react'
import { type RouteObject, useNavigate } from 'react-router-dom'

const ScorePage: FC = () => {
  const navigate = useNavigate()
  const [score, setScore] = useState('...')

  useEffect(() => {
    void fetch('/api/games/calculateScore', { method: 'POST' })
      .then(async (res) => await res.json())
      .then((res) => {
        setScore(res.score.toString())
      })
  }, [])

  return (
    <div>
      점수: {score}
      <button onClick={() => { navigate('/') }}>돌아가기</button>
    </div>
  )
}

export default ScorePage
export const scoreRouter: RouteObject = {
  path: '/score',
  element: <ScorePage />
}

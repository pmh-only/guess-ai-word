import { useEffect, type FC } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'

import style from './style.module.scss'
import { toast } from 'react-hot-toast'

const GameCreationPage: FC = () => {
  const location = useLocation()
  const navigate = useNavigate()

  useEffect(() => {
    void (async () => {
      const { gameType, dictionaryCategory } = location.state

      if (gameType === undefined && dictionaryCategory === undefined) {
        navigate('/', { replace: true })
        return
      }

      const apiResponse = await fetch('/api/games/createNewGame', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          gameType, dictionaryCategory
        })
      })

      if (apiResponse.status !== 200) {
        toast.error('게임 생성중 오류가 발생했습니다. 잠시후 다시 시도해 주세요')
        navigate('/', { replace: true })
        return
      }

      setTimeout(() => {
        navigate('/ingame/createNewRound', {
          replace: true
        })
      }, 1000)
    })()
  }, [])

  return (
    <div className={style.title}>
      <h1>Guess<span>AI</span>Word</h1>
      <p>GPT와 함께하는 단어 맞추기 게임</p>
    </div>
  )
}

export default GameCreationPage

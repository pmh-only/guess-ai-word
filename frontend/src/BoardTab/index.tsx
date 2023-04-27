import { useState, type FC, useEffect } from 'react'

import style from './style.module.scss'
import TitleBar from '../TitleBar'

interface GameDetail {
  dictionaryCategory: string
  finalScore: number
  gameType: string
  id: number
  playerName: string | null
  rounds: Array<{
    id: number
    answer: string
    chosungHintShowed: boolean
    correctAnswer: boolean
    asks: Array<{
      askPrompt: string
      response: string
    }>
  }>
}

const BoardTab: FC = () => {
  const [isLast, setIsLast] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [lastId, setLastId] = useState(0)
  const [gameDetail, setGameDetail] = useState<GameDetail[]>([])

  const fetchGameHistory = async (): Promise<void> => {
    if (isLast || isLoading) return
    setIsLoading(true)

    const { last, games } = await fetch('/api/games/getGameHistory', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        lastId,
        count: 10
      })
    }).then(async (res) => await res.json())

    setGameDetail([
      ...gameDetail,
      ...games
    ])
    setLastId(games[games.length - 1]?.id ?? 0)
    setIsLast(last)
    setIsLoading(false)
  }

  useEffect(() => {
    void fetchGameHistory()
  }, [])

  return (
    <section className={style.container}>
      <TitleBar title="리더보드" isFreepass />
    </section>
  )
}

export default BoardTab

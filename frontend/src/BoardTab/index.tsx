import { useState, type FC, useEffect } from 'react'
import { UnmountClosed } from 'react-collapse'
import { VariableSizeList } from 'react-window'

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
  const [openGameDetail, setOpenGameDetail] = useState(0)
  const [openGameRoundDetail, setOpenGameRoundDetail] = useState(0)

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
      <VariableSizeList
        height={500}
        width="100%"
        itemSize={(i) => 100}
        itemCount={gameDetail.length}>
        {({ index, style }) => (
          <div style={style}>
            <button onClick={() => { setOpenGameDetail(gameDetail[index].id) }}>#{gameDetail[index].id} {gameDetail[index].finalScore}</button>
            <UnmountClosed isOpened={openGameDetail === gameDetail[index].id}>
              <ul>
                {gameDetail[index].rounds.map((round, i) => (
                  <li key={i}>
                    <button onClick={() => { setOpenGameRoundDetail(round.id) }}>{round.answer}</button>
                    <UnmountClosed isOpened={openGameRoundDetail === round.id}>
                      {round.correctAnswer ? 'a' : 'n'}
                    </UnmountClosed>
                  </li>
                ))}
              </ul>
            </UnmountClosed>
          </div>
        )}
      </VariableSizeList>
    </section>
  )
}

export default BoardTab

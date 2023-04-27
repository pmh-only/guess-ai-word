import { useState, type FC, useEffect } from 'react'

import style from './style.module.scss'
import TitleBar from '../TitleBar'
import { MdArrowForward } from 'react-icons/md'
import { useNavigate } from 'react-router-dom'
import { type GameDetail } from '../datatype'

const BoardTab: FC = () => {
  const navigate = useNavigate()
  const [games, setGames] = useState<Map<string, GameDetail[]>>(new Map())

  const fetchGameLeaderboard = (gameType: string): void => {
    void fetch('/api/games/getLeaderBoard', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ gameType })
    }).then(async (res) => await res.json())
      .then(({ games: receivedGames }) => {
        setGames((games) => {
          games.set(gameType, receivedGames)
          return new Map(games)
        })
      })
  }

  useEffect(() => {
    fetchGameLeaderboard('SPEEDRUN')
    fetchGameLeaderboard('NORMAL')
  }, [])

  return (
    <section className={style.container}>
      <TitleBar title="리더보드" isFreepass />
      <ul className={style.itemList}>
        <li>
          <p>난이도</p>
          <p>스피드런</p>
        </li>
        {games.get('SPEEDRUN')?.map((game, i) => (
          <li key={i}>
            <p><i>{i + 1}</i>등 {game.playerName ?? '익명'}</p>
            <p><i>{game.finalScore}</i>점</p>
            <button onClick={() => {
              navigate('/boards/detail', {
                state: { game }
              })
            }}>
              자세히 <MdArrowForward />
            </button>
          </li>
        ))}
        <li>
          <p>난이도</p>
          <p>노말</p>
        </li>
        {games.get('NORMAL')?.map((game, i) => (
          <li key={i}>
            <p><i>{i + 1}</i>등 {game.playerName ?? '익명'}</p>
            <p><i>{game.finalScore}</i>점</p>
            <button onClick={() => {
              navigate('/boards/detail', {
                state: { game }
              })
            }}>
              자세히 <MdArrowForward />
            </button>
          </li>
        ))}
      </ul>
    </section>
  )
}

export default BoardTab

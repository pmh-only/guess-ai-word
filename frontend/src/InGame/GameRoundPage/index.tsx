import { useEffect, type FC } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'

import style from './style.module.scss'

const GameRoundPage: FC = () => {
  const navigate = useNavigate()
  const location = useLocation()
  const { state } = location

  useEffect(() => {
    if (typeof state?.round !== 'number') {
      navigate('/', { replace: true })
    }

    setTimeout(() => {
      void fetch('/api/games/createNewRound', { method: 'POST' })
        .then(() => { navigate('/ingame', { replace: true, state }) })
    }, 2000)
  }, [])

  if (state === null) {
    return <></>
  }

  return (
    <div className={style.roundNotice}>
      <motion.div
        transition={{ duration: 0.5, delay: 0.3 }}
        initial={{ opacity: 0, rotate: 100 }}
        animate={{ opacity: 1, rotate: 0 }}
        className={style.num}>
        <p>{state.round}</p>
        <p>Round!</p>
      </motion.div>
    </div>
  )
}

export default GameRoundPage

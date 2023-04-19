import { type FC } from 'react'

import style from './style.module.scss'
import { motion } from 'framer-motion'
import { MdPlayArrow } from 'react-icons/md'
import { useNavigate } from 'react-router-dom'

const GameTab: FC = () => {
  const navigate = useNavigate()

  return (
    <section className={style.container}>
      <div className={style.title}>
        <h1>Guess<span>AI</span>Word</h1>
        <p>GPT와 함께하는 단어 맞추기 게임</p>
      </div>

      <p className={style.instruct}><MdPlayArrow /> 버튼을 눌러 플레이!</p>

      <motion.button
        onClick={() => { navigate('/ingame/options') }}
        transition={{ duration: 0.2 }}
        whileTap={{ backgroundColor: 'var(--main-secondary)' }}
        className={style.playBtn}>
        <MdPlayArrow fontSize={45} />
      </motion.button>
    </section>
  )
}

export default GameTab

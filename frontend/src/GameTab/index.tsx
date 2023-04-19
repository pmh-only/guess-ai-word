import { type FC } from 'react'

import style from './style.module.scss'
import { FaPlay, FaPlayCircle } from 'react-icons/fa'

const GameTab: FC = () =>
  <section className={style.container}>
    <div className={style.title}>
      <h1>Guess<span>AI</span>Word</h1>
      <p>GPT와 함께하는 단어 맞추기 게임</p>
    </div>

    <p className={style.instruct}><FaPlayCircle /> 버튼을 눌러 플레이!</p>

    <button className={style.playBtn}>
      <FaPlay />
    </button>
  </section>

export default GameTab

import { useState, type FC } from 'react'
import TitleBar from '../GameTitleBar'
import { MdBook, MdPlayCircle, MdStairs } from 'react-icons/md'
import style from './style.module.scss'

import { motion } from 'framer-motion'
import { useNavigate } from 'react-router-dom'

const GameOptionPage: FC = () => {
  const navigate = useNavigate()
  const [gameType, setGameType] = useState('NORMAL')
  const [wordCategory, setWordCategory] = useState('ANY')

  return (
    <article>
      <TitleBar title="게임 옵션 선택" />

      <div className={style.optionContainer}>
        <div className={style.optionTitle}>
          <MdStairs />
          <h3>게임 난이도</h3>
        </div>

        <form className={style.choice}>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { setGameType('NORMAL') }}>
            <input checked={gameType === 'NORMAL'} type="radio" name="gameType" />
            <div>
              <p>노말</p>
              <p>1라운드, 질문 힌트 5회</p>
            </div>
          </motion.label>

          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { setGameType('SPEEDRUN') }}>
            <input checked={gameType === 'SPEEDRUN'} type="radio" name="gameType" id="gameType_speedrun" />
            <div>
              <p>스피드런</p>
              <p>5라운드, 질문 힌트 각 5회</p>
            </div>
          </motion.label>
        </form>

        <hr />

        <div className={style.optionTitle}>
          <MdBook />
          <h3>단어 카테고리</h3>
        </div>

        <form className={style.choice}>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { setWordCategory('ANY') }}>
            <input checked={wordCategory === 'ANY'} type="radio" name="wordCategory" />
            <div>
              <p>전체</p>
              <p>아무 단어나 상관 없어요!</p>
            </div>
          </motion.label>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { setWordCategory('ANIMALS') }}>
            <input checked={wordCategory === 'ANIMALS'} type="radio" name="wordCategory" />
            <div>
              <p>동물</p>
              <p>오리, 사자, 고양이, 개 등...</p>
            </div>
          </motion.label>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { setWordCategory('FRUITS') }}>
            <input checked={wordCategory === 'FRUITS'} type="radio" name="wordCategory" />
            <div>
              <p>과일/채소</p>
              <p>귤, 오렌지, 사과, 배 등...</p>
            </div>
          </motion.label>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { setWordCategory('TOOLS') }}>
            <input checked={wordCategory === 'TOOLS'} type="radio" name="wordCategory" />
            <div>
              <p>도구</p>
              <p>드라이버, 자, 젖병 등...</p>
            </div>
          </motion.label>
        </form>
      </div>

      <nav className={style.bottomNav}>
        <div className={style.btnBar}>
          <motion.button
            whileTap={{ color: 'var(--main-secondary)' }}
            onClick={() => { navigate('/') }}>
              취소
          </motion.button>
          <motion.button
            whileTap={{ backgroundColor: 'var(--main-secondary)' }}
            onClick={() => { navigate(`/ingame/createGame?gameType=${gameType}&wordCategory=${wordCategory}`) }}>
              <MdPlayCircle size={24} />
              플레이
          </motion.button>
        </div>
      </nav>
    </article>
  )
}

export default GameOptionPage

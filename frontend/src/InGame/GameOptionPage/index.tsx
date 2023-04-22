import { useState, type FC } from 'react'
import TitleBar from '../../GameTitleBar'
import { MdBook, MdPlayCircle, MdStairs } from 'react-icons/md'
import style from './style.module.scss'

import { AnimatePresence, motion } from 'framer-motion'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-hot-toast'

const GameOptionPage: FC = () => {
  const navigate = useNavigate()
  const [gameType, setGameType] = useState('NORMAL')
  const [dictionaryCategory, setdictionaryCategory] = useState('ANY')
  const [isLoading, setIsLoading] = useState(false)

  const createGame = async (): Promise<void> => {
    if (isLoading) return
    setIsLoading(true)

    const apiResponse = await fetch('/api/games/createNewGame', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        gameType,
        dictionaryCategory
      })
    })

    if (apiResponse.status !== 200) {
      toast.error('게임 생성중 오류가 발생했습니다. 잠시후 다시 시도해 주세요.')
      setIsLoading(false)
      navigate('/', { replace: true })
      return
    }

    navigate('/ingame/roundNotice', {
      replace: true,
      state: {
        round: 1
      }
    })
  }

  return (
    <article>
      <TitleBar title="게임 옵션 선택" />

      <div className={style.optionContainer}>
        <div className={style.optionTitle}>
          <MdStairs />
          <h3>게임 난이도</h3>
        </div>

        <form className={style.choice}>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { isLoading || setGameType('NORMAL') }}>
            <input disabled={isLoading} checked={gameType === 'NORMAL'} type="radio" name="gameType" />
            <div>
              <p>노말</p>
              <p>1라운드, 질문 힌트 5회</p>
            </div>
          </motion.label>

          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { isLoading || setGameType('SPEEDRUN') }}>
            <input disabled={isLoading} checked={gameType === 'SPEEDRUN'} type="radio" name="gameType" id="gameType_speedrun" />
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
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { isLoading || setdictionaryCategory('ANY') }}>
            <input disabled={isLoading} checked={dictionaryCategory === 'ANY'} type="radio" name="dictionaryCategory" />
            <div>
              <p>전체</p>
              <p>아무 단어나 상관 없어요!</p>
            </div>
          </motion.label>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { isLoading || setdictionaryCategory('ANIMALS') }}>
            <input disabled={isLoading} checked={dictionaryCategory === 'ANIMALS'} type="radio" name="dictionaryCategory" />
            <div>
              <p>동물</p>
              <p>오리, 사자, 고양이, 개 등...</p>
            </div>
          </motion.label>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { isLoading || setdictionaryCategory('FRUITS') }}>
            <input disabled={isLoading} checked={dictionaryCategory === 'FRUITS'} type="radio" name="dictionaryCategory" />
            <div>
              <p>과일/채소</p>
              <p>귤, 오렌지, 사과, 배 등...</p>
            </div>
          </motion.label>
          <motion.label whileTap={{ backgroundColor: 'var(--main-secondary)' }} onClick={() => { isLoading || setdictionaryCategory('TOOLS') }}>
            <input disabled={isLoading} checked={dictionaryCategory === 'TOOLS'} type="radio" name="dictionaryCategory" />
            <div>
              <p>도구</p>
              <p>드라이버, 자, 젖병 등...</p>
            </div>
          </motion.label>
        </form>
      </div>

      <AnimatePresence>
        {!isLoading && (
          <motion.nav
            exit={{ translateY: 100 }}
            className={style.bottomNav}>
            <div className={style.btnBar}>
              <motion.button
                disabled={isLoading}
                whileTap={{ color: 'var(--main-secondary)' }}
                onClick={() => { isLoading || navigate('/') }}>
                  취소
              </motion.button>
              <motion.button
                disabled={isLoading}
                whileTap={{ backgroundColor: 'var(--main-secondary)' }}
                onClick={() => { void createGame() }}>
                  <MdPlayCircle size={24} />
                  플레이
              </motion.button>
            </div>
          </motion.nav>
        )}
      </AnimatePresence>
    </article>
  )
}

export default GameOptionPage

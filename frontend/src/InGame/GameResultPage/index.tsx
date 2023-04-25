import { useState, type FC, useEffect } from 'react'
import CountUp from 'react-countup'
import TitleBar from '../../GameTitleBar'
import { AnimatePresence, motion } from 'framer-motion'

import style from './style.module.scss'
import { MdCheck, MdPerson } from 'react-icons/md'
import { useNavigate } from 'react-router-dom'

const GameResultPage: FC = () => {
  const [score, setScore] = useState(0)
  const [playerNameModal, showPlayerNameModal] = useState(false)
  const [playerName, setPlayerName] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    void fetch('/api/games/calculateScore', { method: 'POST' })
      .then(async (res) => await res.json())
      .then(({ score }) => { setScore(score) })
  }, [])

  const submitPlayerName = async (): Promise<void> => {
    if (playerName.length < 1) return

    showPlayerNameModal(false)

    await fetch('/api/games/updatePlayerName', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        playerName
      })
    })

    navigate('/', {
      replace: false
    })
  }

  return (
    <div className={style.container}>
      <TitleBar title="게임 결과" />

      <div className={style.final}>
        <p>최종 점수</p>
        <div className={style.score}>
          <CountUp
            className={style.count}
            end={score}/>점
        </div>
      </div>

      <nav
        className={style.bottomNav}>
        <div className={style.btnBar}>
          <motion.button
            whileTap={{ color: 'var(--main-secondary)' }}
            onClick={() => { navigate('/', { replace: false }) }}>
              익명으로
          </motion.button>
          <motion.button
            whileTap={{ backgroundColor: 'var(--main-secondary)' }}
            onClick={() => { showPlayerNameModal(true) }}>
              <MdCheck size={24} />
              점수등록
          </motion.button>
        </div>
      </nav>

      <AnimatePresence>
        {playerNameModal && (
          <motion.div
            className={style.closeModal}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}>
            <div className={style.modalBg} onClick={() => { showPlayerNameModal(false) }}></div>
            <div className={style.modalContainer}>
              <MdPerson size={24} />
              <h3>스코어보드 점수 등록</h3>
              <p>스코어보드에 등록할 플레이어 이름을 입력해 주세요.</p>

              <input
                autoFocus
                value={playerName}
                enterKeyHint='done'
                onChange={(e) => { setPlayerName(e.target.value.trim()) }}
                type="text" placeholder='여기를 눌러 입력' />

              <div className={style.btnBar}>
                <motion.button
                  whileTap={{ color: 'var(--main-secondary)' }}
                  onClick={() => { showPlayerNameModal(false) }}>
                    취소
                </motion.button>
                <motion.button
                  whileTap={{ backgroundColor: 'var(--main-secondary)' }}
                  onClick={() => { void submitPlayerName() }}>
                    등록
                </motion.button>
              </div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  )
}

export default GameResultPage

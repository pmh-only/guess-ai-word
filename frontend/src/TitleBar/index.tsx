import { useState, type FC } from 'react'
import { MdArrowBack, MdRefresh } from 'react-icons/md'

import style from './style.module.scss'
import { AnimatePresence, motion } from 'framer-motion'
import { useNavigate } from 'react-router-dom'

interface Props {
  title: string
  isFreepass?: boolean
}

const TitleBar: FC<Props> = ({ title, isFreepass = false }) => {
  const [showCloseModal, setShowClassModal] = useState(false)
  const navigate = useNavigate()

  return (
    <>
      <nav className={style.titleBar}>
        <motion.button
          onClick={() => {
            isFreepass
              ? navigate('/')
              : setShowClassModal(true)
          }}
          whileTap={{ backgroundColor: 'var(--main-secondary)' }}
          className={style.backBtn}>
          <MdArrowBack size={24} />
        </motion.button>
        <h2>{title}</h2>
      </nav>
      <AnimatePresence>
        {showCloseModal && (
          <motion.div
            className={style.closeModal}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}>
            <div className={style.modalBg} onClick={() => { setShowClassModal(false) }}></div>
            <div className={style.modalContainer}>
              <MdRefresh size={24} />
              <h3>진행중인 게임을 종료할까요?</h3>
              <p>이때까지의 게임 진행상황을 삭제하고 홈화면으로 돌아갈게요</p>

              <div className={style.btnBar}>
                <motion.button
                  whileTap={{ color: 'var(--main-secondary)' }}
                  onClick={() => { setShowClassModal(false) }}>
                    아니요
                </motion.button>
                <motion.button
                  whileTap={{ backgroundColor: 'var(--main-secondary)' }}
                  onClick={() => { setShowClassModal(false); navigate('/') }}>
                    네
                </motion.button>
              </div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </>
  )
}

export default TitleBar

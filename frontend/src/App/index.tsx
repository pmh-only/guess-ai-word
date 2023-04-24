import { type FC } from 'react'
import Container from '../Container'
import BottomTabNavigation from '../BottomTabNavigation'
import { Route, Routes, useLocation } from 'react-router-dom'
import GameTab from '../GameTab'
import CreditTab from '../CreditTab'
import BoardTab from '../BoardTab'

import { AnimatePresence } from 'framer-motion'

import style from './style.module.scss'
import PageWrapper from './PageWrapper'
import GameOptionPage from '../InGame/GameOptionPage'
import { Toaster } from 'react-hot-toast'
import GameRoundPage from '../InGame/GameRoundPage'
import InGamePage from '../InGame/InGamePage'

const App: FC = () => {
  const location = useLocation()

  return (
    <Container>
      <main className={style.main}>
        <AnimatePresence>
          <Routes location={location} key={location.pathname}>
            <Route path="/" element={<PageWrapper><GameTab /></PageWrapper>} />
            <Route path="/credits" element={<PageWrapper><CreditTab /></PageWrapper>} />
            <Route path="/boards" element={<PageWrapper><BoardTab /></PageWrapper>} />
            <Route path="/ingame" element={<PageWrapper><InGamePage /></PageWrapper>} />
            <Route path="/ingame/options" element={<PageWrapper><GameOptionPage /></PageWrapper>} />
            <Route path="/ingame/roundNotice" element={<PageWrapper><GameRoundPage /></PageWrapper>} />
          </Routes>
        </AnimatePresence>
      </main>

      <BottomTabNavigation />
      <Toaster toastOptions={{
        iconTheme: {
          primary: 'var(--main-accent)',
          secondary: 'var(--main-secondary)'
        },
        position: 'top-center',
        style: {
          backgroundColor: 'var(--sub-bg)',
          borderRadius: 16,
          boxShadow: 'none'
        }
      }} />
    </Container>
  )
}

export default App

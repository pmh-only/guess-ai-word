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
          </Routes>
        </AnimatePresence>
      </main>

      <BottomTabNavigation />
    </Container>
  )
}

export default App

import { type FC } from 'react'
import { createHashRouter, RouterProvider } from 'react-router-dom'
import { askingRoute } from '../AskingPage'
import { ingameRouter } from '../IngamePage'
import { introRoute } from '../IntroPage'
import { scoreRouter } from '../ScorePage'

const router = createHashRouter([
  introRoute,
  askingRoute,
  ingameRouter,
  scoreRouter
])

const App: FC = () =>
  <RouterProvider router={router} />

export default App

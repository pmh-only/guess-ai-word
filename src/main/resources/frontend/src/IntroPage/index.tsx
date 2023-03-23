import { type FormEvent, type FC, useState } from 'react'
import { useNavigate, type RouteObject } from 'react-router-dom'

const IntroPage: FC = () => {
  const navigate = useNavigate()
  const [type, setType] = useState('NORMAL_GAME')
  const [category, setCategory] = useState('ANY')
  const [isDisabled, setDisabled] = useState(false)

  const onSubmit = (e: FormEvent): void => {
    e.preventDefault()

    if (isDisabled) return
    setDisabled(true)

    navigate('/asking', {
      state: { type, category }
    })
  }

  return (
    <main>
      <div>
        <div>
          <h1>GuessAIWord</h1>
          <p>Backend testing prototype</p>
        </div>

        <form onSubmit={onSubmit}>
          <select disabled={isDisabled} value={type} onChange={(e) => { setType(e.target.value) }}>
            <option value="NORMAL_GAME">노말</option>
            <option value="SPEED_RUN">스피드런</option>
          </select>

          <select disabled={isDisabled} value={category} onChange={(e) => { setCategory(e.target.value) }}>
            <option value="ANY">아무단어</option>
            <option value="ANIMAL">동물</option>
            <option value="FOOD">음식</option>
            <option value="FURNITURE">가구</option>
            <option value="TOOLS">도구</option>
          </select>

          <button disabled={isDisabled}>시작하기</button>
        </form>
      </div>
    </main>
  )
}

export default IntroPage
export const introRoute: RouteObject = {
  path: '/',
  element: <IntroPage />
}

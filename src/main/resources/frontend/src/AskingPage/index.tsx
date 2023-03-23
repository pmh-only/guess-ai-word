import { useEffect, useState, type FC } from 'react'
import { useLocation, useNavigate, type RouteObject } from 'react-router-dom'

const AskingPage: FC = () => {
  const [isDisabled, setDisabled] = useState(false)
  const navigate = useNavigate()
  const location = useLocation()

  const fetchData = (): void => {
    const { type, category } = location.state as Record<string, string>
    void fetch(`/api/games/createNewGame?type=${type}&category=${category}`, { method: 'POST' })
      .then(async (res) => await res.json())
      .then((res) => {
        if (res.error !== undefined) {
          fetchData()
          return
        }

        navigate('/ingame', {
          state: { type, category, gameData: res }
        })
      })
  }

  useEffect(() => {
    if (isDisabled) return
    setDisabled(true)
    fetchData()
  }, [])

  return (
    <>질문 중... (보통 20초에서 30초 정도 걸립니다.)</>
  )
}

export default AskingPage
export const askingRoute: RouteObject = {
  path: '/asking',
  element: <AskingPage />
}

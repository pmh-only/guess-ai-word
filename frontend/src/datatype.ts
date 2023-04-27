export interface GameDetail {
  dictionaryCategory: string
  finalScore: number
  gameType: string
  id: number
  playerName: string | null
  rounds: Array<{
    id: number
    answer: string
    chosungHintShowed: boolean
    correctAnswer: boolean
    asks: Array<{
      askPrompt: string
      response: string
    }>
  }>
}

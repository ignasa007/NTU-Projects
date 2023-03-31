import axios from 'axios'

export const scheduler = (flashcardsToStudy) => {
  const flashcards = axios.get(
    `http://localhost:5000/flashcard/get-due-flashcards/${folderId}`,
    {
      params: {
        dueDate: 'insert due-date here',
        dirStructire: 'insert dir-structure here',
      },
    },
  )

  flashcards.forEach((flashcard) => {
    const editedFlashcard = {
      id: flashcard.currentId,
      index: flashcard.currentIndex,
      dueDate: flashcard.currentDate,
      level: newSelectedLevel,
      textFront: flashcard.textFront,
      textBack: flashcard.textBack,
      hints: flashcard.hints,
    }
    axios({
      url: `http://localhost:5000/flashcard/update-due-flashcards/${folderId}/${editedFlashcard.currentId}`,
      method: 'PUT',
      data: {
        flashcard: editedFlashcard,
        difficulty: 'easy or hard or medium depending on what user choose',
      },
    })
      .then((value) => console.log(value, 'has been successful edited'))
      .catch((err) => console.log('following error has occured: \n', err))
  })
}

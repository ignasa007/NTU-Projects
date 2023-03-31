import React from 'react'
import { useState } from 'react'
import { withRouter } from 'react-router'
import { Switch, Link, Route, BrowserRouter } from 'react-router-dom'
import FolderScreen from '../folder-screens/FolderScreen'
import StudyScreen from '../flashcard-screens/StudyScreen'

import './SelectFoldersScreen.css'
import '../../App.scss'

import './SelectFoldersScreen.css'

function SelectFoldersScreen(props) {
  const { history } = props

  const folderDir = ['CZ3002', 'CZ3005', 'CZ3006']

  const [selectedFolders, setSelected] = useState([])

  const addSelected = (event) => {
    // setSelected(selectedFolders => [...selectedFolders, event.target.value])
    // setSelected(selectedFolders => ({
    //     selectedFolders: [...selectedFolders, event.target.value]}))
    setSelected((selectedFolders) => selectedFolders.concat(event.target.value))
    console.log(selectedFolders)
  }

  const removeSelected = (event) => {
    setSelected({
      folder: selectedFolders.filter((folder) => folder !== event.target.value),
    })
    console.log(selectedFolders)
  }

  const handleChange = (event) => {
    const checked = event.target.checked
    if (checked) {
      addSelected(event)
    } else {
      removeSelected(event)
    }
  }

  const showFoldersToChoose = (folderDir) => {
    return folderDir.map((folder) => {
      return (
        <div>
          <label class="inline-flex">
            <input type="checkbox" value={folder} onClick={handleChange} />{' '}
            {folder}
          </label>
        </div>
      )
    })
  }

  const handleSubmit = () => {
    console.log('Selected folders: ' + selectedFolders)
    alert('Selected folders: ' + selectedFolders)
    // return(
    //     <div>
    //         <Switch>
    //             <Link to = '/home/folders/home' component={FolderScreen} />
    //         </Switch>
    //     </div>
    // )
  }

  return (
    <body className="App-body">
      <h1>Select Folders to Study</h1>
      <div className="popup">
        <div>{showFoldersToChoose(folderDir)}</div>

        <div className="flex flex-end">
          <button className="btn" onClick={() => handleSubmit}>
            Submit
          </button>
        </div>
      </div>
    </body>
  )
}

export default withRouter(SelectFoldersScreen)
// export default SelectFoldersScreen

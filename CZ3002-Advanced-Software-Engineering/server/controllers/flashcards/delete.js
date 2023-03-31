import folderModel from '../../models/folder.js';
import flashcardModel from '../../models/flashcard.js';
import { process } from '../utils.js';

export const deleteFlashcard = async (req, res) => {
  
  var { path, dirStructure } = req.body;
  console.log(path, dirStructure);
  const { arr } = process(path);

  if (arr.length == 0) {

    let message = "Check the path. Couldn't parse.";
    console.log(message);
    res.status(400).json(Error({ message: message }));

  } else if (arr[0] === '.') {

    if (arr.length == 1) {

      console.log('Only root provided? Are you stupid?');
      res.status(400).json(Error({ message: 'Check the path. Only root provided.' }));

    } else {

      try {

        // if path = ./CZ3005/MDP/123, then command = "dirStructure['CZ3005']['MDP']"
        let command = 'dirStructure';
        for (let i=1; i<arr.length-1; i++) {
          command = command + `['${arr[i]}']`;
        }
        var index = -1;
        eval(`index = ${command}.indexOf(arr[arr.length-1])`)
        command = command + '.splice(index, 1)';
        eval(command);

        let flashcardDeletePromise = flashcardModel.deleteOne({ _id: arr[arr.length - 1] });
        let folderUpdatePromise = folderModel.updateOne(
          {  _id: req.params.id },
          { contents: dirStructure }
        );

        console.log(`Deleted flashcard ${path}`);
        console.log(dirStructure);
        res.status(200).json(dirStructure);

        await Promise.all([ flashcardDeletePromise, folderUpdatePromise ]);

      } catch (error) {

        console.log(error.stack);
        res.status(500).json(Error({ message: error.message }));

      }

    }

  } else {

    let message = 'First folder should be root.';
    console.log(message);
    res.status(400).json(Error({ message: message }));

  }

}

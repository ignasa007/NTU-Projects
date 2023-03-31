import { getFlashcardsByFolder } from "./read.js";
import { process } from '../utils.js';

export const exportFlashcards = async(req, res) => {

    let { path, dirStructure } = req.query;
    dirStructure = JSON.parse(dirStructure);
    
    let { arr, processedPath } = process(path);

    if (arr.length == 0) {

        let message = "Check the path. Couldn't parse.";
        console.log(message);
        res.status(400).json(Error({ message: message }));

    } else if (arr[0] === '.') {

        try {

            // if path = ./CZ3005/MDP, then command = "dirStructure['CZ3005']['MDP']"
            let folder = null;
            let command = 'folder = dirStructure';
            for (let i=1; i<arr.length; i++) {
                command = command + `['${arr[i]}']`;
            }
            eval(command);

            const { contents, paths } = await getFlashcardsByFolder(folder, processedPath.slice(2));
            var fileContents = [];
            
            for (let i=0; i<contents.length; i++) {
                fileContents.push({
                    title: contents[i].title,
                    level: contents[i].level,
                    front: contents[i].front,
                    back: contents[i].back,
                    hints: contents[i].hints
                });
            };

            res.status(200).json({ 
                fileContents: fileContents, 
                relativePaths: paths 
            });

        } catch (error) {
            console.log(error.stack);
            res.status(500).json(Error({ message: error.message }));
        }

    } else {

        let message = `First folder should be root. Invalid path ${path}.`;
        console.log(message)
        res.status(400).json(Error({ message: message }));  

    }

}
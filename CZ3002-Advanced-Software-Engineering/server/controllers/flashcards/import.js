import { createFlashcards } from './create.js';

export const importFlashcards = async(req, res) => {

    let { fileContents, relativePaths, dirStructure, insertPath } = req.body;
    let absolutePaths = [];
    relativePaths.forEach((relativePath) => {
        absolutePaths.push(insertPath + '/' + relativePath.replace(/\/(?:.(?!\/))+$/i, ''));
    })

    let { updatedDirStructure } = await createFlashcards(req.params.id, dirStructure, fileContents, absolutePaths);
    console.log(updatedDirStructure);
    res.status(201).json(updatedDirStructure);

}
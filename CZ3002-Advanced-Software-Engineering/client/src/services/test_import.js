import JSZip from 'jszip';
import api from './api';

export const upload = async(zipFiles) => {

    let dirStructure = {}, data = [], insertPath = '.';

    for (let i=0; i<zipFiles.length; i++) {
        data.push(process(zipFiles[i]));
    }

    var fileContents = [], relativePaths = [];
    await Promise.all(data)
        .then((zipFilesContent) => {
            zipFilesContent.forEach((zipFileContent) => {
                zipFileContent.forEach((file) => {
                    if (file.content !== '') {
                        fileContents.push(JSON.parse(file.content));
                        relativePaths.push(file.path);
                    }
                });
            });
        });

    return api.post('/flashcard/import-flashcards/246', {
        fileContents: fileContents,
        relativePaths: relativePaths,
        dirStructure: dirStructure,
        insertPath: insertPath
    })
        .then(async(res) => {
            if (res.status === 201) {
                return "SUCCESS";
            }
            else {
                console.log("Failed to import flashcards.");
                return "FAILURE";
            }
        }).catch(error => {
            console.log(error);
            return "ERROR";
        })

}


async function process(zipFile) {

    return new Promise((resolve, reject) => {
        JSZip.loadAsync(zipFile)                                   
            .then(async(zip) => {
                let zipContent = [], promises = [];
                zip.forEach(async(relativePath, file) => {
                    const promise = file.async('string');
                    promises.push(promise);
                    zipContent.push({
                        path: relativePath,
                        content: await promise
                    });
                });
                await Promise.all(promises);
                resolve(zipContent);
            })
            .catch((error) => {
                reject(error);
            });
    });

}
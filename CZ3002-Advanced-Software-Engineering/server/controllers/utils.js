export const process = (path) => {

    // assuming / to be the separator
    // replace multiple forward slashes with single and remove any trailing slashes
    path = path.replace(/\/+/, '/').replace(/\/$/, '');
    const arr = path.split('/');

    return {
        arr: arr,
        processedPath: path
    };

}

export const sleep = (time) => {

    return new Promise((resolve) => setTimeout(resolve, time));
    
}
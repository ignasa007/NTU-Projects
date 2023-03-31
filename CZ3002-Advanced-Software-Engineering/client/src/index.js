import React from 'react';
import ReactDOM from 'react-dom';
import Amplify from 'aws-amplify';

import './index.css'
import App from './App.js'
import config from './config.js'

Amplify.configure(config.cognito);

ReactDOM.render(<App />, document.getElementById('root'))

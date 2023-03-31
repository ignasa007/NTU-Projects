import { Auth } from 'aws-amplify';

export const forgotPassword = async(req, res) => {

    const { username } = req.body;

    Auth.forgotPassword(username) 
        .then(data => {
            console.log(`Reset instructions sent over ${data.CodeDeliveryDetails.DeliveryMedium} to ${data.CodeDeliveryDetails.Destination}.`);
            res.status(202);
        })
        .catch(error => {
            console.log(`Error sending reset instructions. ${error.name}: ${error.message}.`);
            res.status(500).json(error);
        });

}

export const resetPassword = async(req, res) => {

    const { username, code, new_password } = req.body;

    Auth.forgotPasswordSubmit(username, code, new_password)
        .then(data => {
            console.log(`Successfully changed password for ${username}.`);
            res.status(204);
        })
        .catch(error => {
            console.log(`Error resetting password. ${error.name}: ${error.message}`);
            res.status(500).json(error);
        });

}

export const changePassword = async(req, res) => {

    const { old_password, new_password } = req.body;
    
    Auth.currentAuthenticatedUser()
        .then(async(user) => {
            await user.getUserAttributes(async(error, data) => {
                if (error) {
                    console.log(error.message);
                    return undefined;
                } else {
                    data.forEach((datum) => {
                        if (datum.getName() == "email") {
                            console.log(`Changing password for ${datum.getValue()}.`);
                        } 
                    });
                }
            });            
            return await Auth.changePassword(user, old_password, new_password);
        })
            .then(data => {
                console.log(data); // SUCCESS message
                res.status(201);
            })
            .catch(error => {
                console.log(error);
                res.status(500).json(error);
            });

}
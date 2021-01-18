Initially, to use the application changes must be made in the code
At Webpage line:13 the connector must be set with postgres details

needs postgresql url, postgres username, postgres password.

At MainDemo line:7 the name of the repository needs to be set, the path for where the images will be stored locally

AFTER RUNNING APPLICATION

To use the application login with Username and Password, for the sake of demo the user already added is "TipTop" with password "a123a123";
Once the credentials are verified the selection panel will display. 

ADDING IMAGE 
To add an image to the repository the url of the image and a given name is used. The name must be unique to the user. If the text is reset to blank it means the adding was successful. If the text doesn't disappear there was an error (either the name wasn't unique or the image couldn't be downloaded). 

The image information is then added to the database and the image is saved to the folder given. 

GETTING IMAGE TO DISPLAY
An image can be searched by name, a user can only get their own images. If the image exists it will be displayed and the text box will be reset. 

GETTING IMAGES
This button will get all images added by the logged in user. The user can cycle through the images they've added to the repository.

DELETING REPOSITORY
Upon exiting the JFrame the folder containing the images will be deleted as will the tables keeping track of users and images.



Methods have been created internally to register a user, and delete a user or image. These methods have not had graphic interfaces to interact with.





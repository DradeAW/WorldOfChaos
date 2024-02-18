World of Chaos is a 2D RPG made by Aurélien WYNGAARD (coding) and Gwenaël LE HIR (Graphics Design).

It is made in Java using LWJGL 2.
It is meant to me compiled and ran using IntelliJ IDEA.

In order to configure the libraries with IntelliJ IDEA:
- Go to the Project Structure (Ctrl+Shift+Alt+S)
- Under "Project Settings", select Libraries
- Click add and select lwjgl.jar

Then, to link the dll:
- Go in the "Edit Configurations" for the compiler
- Under VM options, put: "-Djava.library.path=lib/{your_os}"

To configure the annotations:
- Go to the Project Structure (Ctrl+Shift+Alt+S)
- Under "Project Settings", select Libraries
- Click add --> from Maven --> type in "org.jetbrains:annotations:24.1.0"

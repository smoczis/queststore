package controller;

import model.usr.*;
import view.*;
import java.util.ArrayList;

public class MentorController extends UserController {

    ConsoleMentorView view;

    public MentorController() {

        view = new ConsoleMentorView();
        super.view = view;
    }

    public String getUserType() {

        return "Mentor";
    }


    public void start (User user) {
        if (user.getClass().getSimpleName().equals("Mentor")) {
            setUser(user);
            handleMainMenu();
        } else {
            handleSupervisorMenu();
        }
    }

    private void handleMainMenu() {
        boolean isBrowsed = true;
        while(isBrowsed){
            view.clearScrean();
            view.showShortInfo(user);
            Integer choice = view.handleMainMenu();
            if(choice == 1){
                view.showFullInfo(user);
                editUserData(user);
            } else if(choice == 2) {
                StudentController studentController = new StudentController();
                studentController.start(user);
            } else if(choice == 3) {
                GroupController groupController = new GroupController();
                groupController.start(user);
            } else if(choice == 4) {
                ClassController classController = new ClassController();
                classController.start(user);
            } else if(choice == 5) {
                QuestController questController = new QuestController();
                questController.start(user);
            } else if(choice == 6) {
                ArtifactController artifactController = new ArtifactController();
                artifactController.start(user);
            } else if(choice == 0) {
                isBrowsed = false;
            }
        }
    }

    private void handleSupervisorMenu() {
        boolean isBrowsed = true;
        while(isBrowsed){
            view.clearScrean();
            ArrayList<Mentor> mentors = Mentor.getObjects();
            Integer choice = view.handleSupervisorMenu(mentors);
            if(choice == 1){
                Mentor mentor = view.getListChoice(mentors);
                if (mentor != null) {
                    handleDetails(mentor);
                }
            } else if(choice == 2){
                Mentor newMentor = new Mentor();
                editUserData(newMentor);
            } else if(choice == 0){
                isBrowsed = false;
            }
        }
    }

    private void handleDetails(Mentor mentor) {
        boolean isBrowsed = true;
        while(isBrowsed){
            Integer choice = view.handleDetailsMenu(mentor);
            if(choice == 1){
                editUserData(mentor);
            } else if(choice == 2){
                isBrowsed = !Mentor.remove(mentor);
            } else if(choice == 0){
                isBrowsed = false;
            }
        }
    }

}

package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

//import projects.dao.DbConnection;

public class ProjectsApp {
	
	private ProjectService projectService = new ProjectService();
	// @formatter:off
	private List<String> operations = List.of("1) Add a project");
	// @formatter:on
	
	private Scanner scanner = new Scanner (System.in);

	public static void main(String[] args) {
		//DbConnection.getConnection();
		ProjectsApp projectAppObj = new ProjectsApp();
		projectAppObj.processUserSelections();
	}

	private void processUserSelections() {
		boolean done = false;
		
		while (!done) {
			try {
				int selection = getUserSelection();
				switch (selection)
				{
				case -1:
					done = exitMenu();
					break;
				
				case 1:
					createProject();
					break;
					
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.\n");
				}
			}
			catch (Exception e) {
				System.out.println("Error: " + e.toString());
			}
		}
		
	}

	private void createProject() {
		String projectName = getStringInput("Enter the project name ");
		BigDecimal estimatedHours = getDecimalInput("Enter estimated hours ");
		BigDecimal actualHours = getDecimalInput("Enter actual hours ");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) ");
		String notes = getStringInput("Enter the project notes ");
		Project project = new Project();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		
		System.out.println("You have successfully created project: " + dbProject);
	}

	private boolean exitMenu() {
		System.out.println("Exiting menu.");
		return true;
	}

	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		if (input == null) {
			return -1;
		}
		else {
			return input;
		}
	}
	
	private BigDecimal getDecimalInput(String prompt) {
		String input  = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch (NumberFormatException nfe) {
			throw new DbException (input + " is not a valid decimal number.");
			
		}
	}


	private Integer getIntInput(String prompt) {
		String input  = getStringInput(prompt);
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return Integer.valueOf(input);
		}
		catch (NumberFormatException nfe) {
			throw new DbException (input + " is not a valid number. Try again. ");
			
		}
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		
		if (input.isBlank()) {
			return null;
		}
		else {
			return input.trim();
		}
	}

	private void printOperations() {
		System.out.println("These are the available selections. Press the Enter key to quit: ");
		operations.forEach(line -> System.out.println("   " + line));
		
	}
	
	

}

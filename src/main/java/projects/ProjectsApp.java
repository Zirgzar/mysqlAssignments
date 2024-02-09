package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

//	@formatter:off
	private List<String> operations = List.of(
				"1) Add a project",
				"2) List projects",
				"3) Select a project",
				"4) Update project details",
				"5) Delete a project"
			);
//	@formatter:on

	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	}

	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			try {
				int selection = getUserSelection();

				switch (selection) {
				case -1:
					done = exitMenu();
					break;
				case 1:
					createProject();
					break;
				case 2:
					listProjects();
					break;
				case 3:
					selectProject();
					break;
				case 4:
					updateProjectDetails();
					break;
				case 5:
					deleteProject();
					break;
				default:
					System.out.println("\n" + selection + " is not valid. Try again.");
					break;
				}
			} catch (Exception e) {
				System.out.println("\nError: " + e.toString() + " Try again.");
			}
		}
	}

	private void deleteProject() {
		listProjects();
		
		Integer projectId = getIntInput("Enter ID of project to delete.");
		
		if (Objects.nonNull(projectId)) {
			projectService.deleteProject(projectId);
			
			System.out.println("Project with ID " + projectId + " has been deleted.");
			
			if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
				curProject = null;
			}
		}
	}

	private void updateProjectDetails() {
		if (Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project first.");
			return;
		}

		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal projectEstimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal projectActualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer projectDifficulty = getIntInput("Enter the project difficulty [" + curProject.getDifficulty() + "]");
		String projectNotes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");

		Project project = new Project();
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(projectEstimatedHours) ? curProject.getEstimatedHours() : projectEstimatedHours);
		project.setActualHours(Objects.isNull(projectActualHours) ? curProject.getActualHours() : projectActualHours);
		project.setDifficulty(Objects.isNull(projectDifficulty) ? curProject.getDifficulty() : projectDifficulty);
		project.setNotes(Objects.isNull(projectNotes) ? curProject.getNotes() : projectNotes);
		
		projectService.modifyProjectDetails(project);
		curProject = projectService.fetchProjectById(curProject.getProjectId());
	}

	private void selectProject() {
		listProjects();

		Integer projectId = getIntInput("Enter project id");

		curProject = null;

		curProject = projectService.fetchProjectById(projectId);

		if (Objects.isNull(curProject)) {
			System.out.println("Invalid project id entered.");
		}
	}

	private List<Project> listProjects() {
		List<Project> projects = projectService.fetchProjects();

		System.out.println("\nProjects:");

		projects.forEach(
				project -> System.out.println("    " + project.getProjectId() + ": " + project.getProjectName()));

		return projects;
	}

	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)", 1, 5);
		String notes = getStringInput("Enter the project notes");

		Project project = new Project();

		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);

		Project dbProject = projectService.addProject(project);
		System.out.println("Successfully created project: " + dbProject);

		curProject = projectService.fetchProjectById(dbProject.getProjectId());
	}

	private boolean exitMenu() {
		System.out.println("\nExiting the menu.");
		return true;
	}

	private int getUserSelection() {
		printOperations();

		Integer input = getIntInput("Enter a menu selection");

		return Objects.isNull(input) ? -1 : input;
	}

	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid input.");
		}
	}

	private Integer getIntInput(String prompt, Integer rangeStart, Integer rangeEnd) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		if (Integer.valueOf(input) >= rangeStart && Integer.valueOf(input) <= rangeEnd) {
			try {
				return Integer.valueOf(input);
			} catch (NumberFormatException e) {
				throw new DbException(input + " is not a valid input.");
			}
		} else {
			throw new DbException(input + " is not a valid input.");
		}
	}

	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);

		if (Objects.isNull(input)) {
			return null;
		}

		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	private String getStringInput(String prompt) {
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();

		return input.isBlank() ? null : input.trim();
	}

	private void printOperations() {
		System.out.println("\nThese are the available selections. (Press the Enter key to quit)");

		operations.forEach(line -> System.out.println("    " + line));

		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working on a project.");
		} else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

}

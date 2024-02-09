DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
	project_id INT AUTO_INCREMENT NOT NULL,
	project_name VARCHAR(128) NOT NULL,
	estimated_hours DECIMAL(7,2),
	actual_hours DECIMAL(7,2),
	difficulty INT,
	notes TEXT,
	PRIMARY KEY (project_id)
);

CREATE TABLE category (
	category_id INT AUTO_INCREMENT NOT NULL,
	category_name VARCHAR(128) NOT NULL,
	PRIMARY KEY (category_id)
);

CREATE TABLE project_category (
	project_id INT NOT NULL,
	category_id INT NOT NULL,
	FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE,
	FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
	UNIQUE KEY (project_id, category_id)
);

CREATE TABLE material (
	material_id INT AUTO_INCREMENT NOT NULL,
	project_id INT NOT NULL,
	material_name VARCHAR(128) NOT NULL,
	num_required INT,
	cost DECIMAL(7,2),
	PRIMARY KEY (material_id),
	FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE step (
	step_id INT AUTO_INCREMENT NOT NULL,
	project_id INT NOT NULL,
	step_text TEXT NOT NULL,
	step_order INT NOT NULL,
	PRIMARY KEY (step_id),
	FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

-- Full Test Entry

INSERT INTO project (project_id, project_name, estimated_hours, actual_hours, difficulty, notes)
VALUES
(1, 'Replace blinds with curtains', 1, 2, 3, 'Remove blind holder and screw in new curtain holders');
INSERT INTO category (category_id, category_name) VALUES (1, 'Doors and Windows');
INSERT INTO project_category (project_id, category_id) VALUES (1, 1);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, 'Screws', 4, NULL);
INSERT INTO material (project_id, material_name, num_required) VALUES (1, 'Wall Anchors', 4);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Remove old blind holder', 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Screw in new holder for curtain rod (make sure to use wall anchors)', 2);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Put new curtains on curtain rod', 3);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'Put curtain rod on curtain rod holders', 4);

-- Test data

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes)
VALUES
('Test Project', NULL, NULL, NULL, NULL, NULL),
('Sample Project', 1, 1, 1, 'Sample entry for project table');


CREATE TABLE app_user (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,
                       role VARCHAR(50) NOT NULL,
                       password_change_required BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE feedback (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          email VARCHAR(255),
                          message TEXT NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          priority VARCHAR(50),
                          contact_type VARCHAR(50),
                          created_at TIMESTAMP NOT NULL,
                          closed_at TIMESTAMP,
                          note VARCHAR(1000),
                          created_by BIGINT,
                          assigned_to BIGINT,

                          CONSTRAINT fk_feedback_created_by
                              FOREIGN KEY (created_by) REFERENCES app_user(id),

                          CONSTRAINT fk_feedback_assigned_to
                              FOREIGN KEY (assigned_to) REFERENCES app_user(id)
);

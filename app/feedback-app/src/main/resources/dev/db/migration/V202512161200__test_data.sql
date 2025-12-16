INSERT INTO app_user (username, email, password, enabled, role, password_change_required) VALUES
    ('user1', 'user1@feedback.com', '$2a$10$7bN6uA8HJKQH35h5t3ZSe.NA1JtmmXjV3h3/KWdzZlhj7I5rI9uOa', TRUE, 'FEEDBACK_MANAGER', FALSE),
    ('user2', 'user2@feedback.com', '$2a$10$7bN6uA8HJKQH35h5t3ZSe.NA1JtmmXjV3h3/KWdzZlhj7I5rI9uOa', TRUE, 'FEEDBACK_MANAGER', FALSE),
    ('user3', 'user3@feedback.com', '$2a$10$7bN6uA8HJKQH35h5t3ZSe.NA1JtmmXjV3h3/KWdzZlhj7I5rI9uOa', TRUE, 'FEEDBACK_MANAGER', FALSE),
    ('user4', 'user4@feedback.com', '$2a$10$7bN6uA8HJKQH35h5t3ZSe.NA1JtmmXjV3h3/KWdzZlhj7I5rI9uOa', TRUE, 'USER', FALSE),
    ('user5', 'user5@feedback.com', '$2a$10$7bN6uA8HJKQH35h5t3ZSe.NA1JtmmXjV3h3/KWdzZlhj7I5rI9uOa', TRUE, 'READ_ONLY', FALSE);

INSERT INTO feedback (name, email, message, status, priority, contact_type, created_at, closed_at) VALUES
    ('Jean', 'jean@example.com', 'Le site web est très intuitif.', 'OPEN', 'HIGH', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '20' DAY, NULL),
    ('Marie', 'marie@example.com', 'Problème avec la connexion.', 'OPEN', 'MEDIUM', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '19' DAY, NULL),
    ('Paul', 'paul@example.com', 'Suggestion pour améliorer la navigation.', 'OPEN', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '18' DAY, NULL),
    ('Lucie', 'lucie@example.com', 'Erreur lors du paiement.', 'CLOSED', 'HIGH', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '17' DAY, CURRENT_TIMESTAMP - INTERVAL '16' DAY),
    ('Marc', 'marc@example.com', 'Le formulaire de contact ne fonctionne pas.', 'OPEN', 'MEDIUM', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '16' DAY, NULL),
    ('Sophie', 'sophie@example.com', 'Bonne expérience utilisateur.', 'CLOSED', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '15' DAY, CURRENT_TIMESTAMP - INTERVAL '14' DAY),
    ('Antoine', 'antoine@example.com', 'Besoin d’aide pour l’inscription.', 'OPEN', 'HIGH', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '14' DAY, NULL),
    ('Claire', 'claire@example.com', 'Suggestion: Ajouter une FAQ.', 'OPEN', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '13' DAY, NULL),
    ('Julien', 'julien@example.com', 'Problème avec l’envoi des emails.', 'OPEN', 'HIGH', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '12' DAY, NULL),
    ('Emma', 'emma@example.com', 'Merci pour le support rapide.', 'CLOSED', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '11' DAY, CURRENT_TIMESTAMP - INTERVAL '10' DAY),
    ('Thomas', 'thomas@example.com', 'Page lente à charger.', 'OPEN', 'MEDIUM', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '10' DAY, NULL),
    ('Chloé', 'chloe@example.com', 'Erreur 404 sur la page contact.', 'OPEN', 'HIGH', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '9' DAY, NULL),
    ('Nicolas', 'nicolas@example.com', 'Proposition: ajouter un chat en ligne.', 'OPEN', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '8' DAY, NULL),
    ('Laura', 'laura@example.com', 'Impossible de télécharger le rapport.', 'OPEN', 'MEDIUM', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '7' DAY, NULL),
    ('Vincent', 'vincent@example.com', 'Page FAQ très utile.', 'CLOSED', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '6' DAY, CURRENT_TIMESTAMP - INTERVAL '5' DAY),
    ('Elodie', 'elodie@example.com', 'Erreur lors du changement de mot de passe.', 'OPEN', 'HIGH', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '5' DAY, NULL),
    ('Kevin', 'kevin@example.com', 'Bonne réactivité du support.', 'CLOSED', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '4' DAY, CURRENT_TIMESTAMP - INTERVAL '3' DAY),
    ('Isabelle', 'isabelle@example.com', 'Problème sur mobile.', 'OPEN', 'MEDIUM', 'SUPPORT', CURRENT_TIMESTAMP - INTERVAL '3' DAY, NULL),
    ('Damien', 'damien@example.com', 'Améliorer la section contact.', 'OPEN', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '2' DAY, NULL),
    ('Alice', 'alice@example.com', 'Site très complet.', 'CLOSED', 'LOW', 'GENERAL', CURRENT_TIMESTAMP - INTERVAL '1' DAY, CURRENT_TIMESTAMP);
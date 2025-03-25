CREATE TABLE configuration (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    logo VARCHAR(255),
    daily_rate DECIMAL(10,2) NOT NULL,
    late_fee DECIMAL(10,2) NOT NULL,
    loss_fee DECIMAL(10,2) NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE publisher (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL
);

CREATE TABLE author (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    nationality VARCHAR(100),
    birth_date DATE
);

CREATE TABLE book (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    author_id UUID,
    publisher_id UUID,
    title VARCHAR(255) NOT NULL,
    code VARCHAR(20) UNIQUE NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    quantity INT DEFAULT 1,
    publication_date DATE,
    available_copies INT DEFAULT 1,
    price DECIMAL(10,2),
    image_url VARCHAR(255),
    FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE SET NULL,
    FOREIGN KEY (publisher_id) REFERENCES publisher(id) ON DELETE SET NULL
);

CREATE TABLE degree (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code BIGINT UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE role (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL
);

CREATE TABLE user_account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    cui BIGINT UNIQUE NOT NULL,
    birth_date DATE,
    role_id UUID,
    is_approved BOOLEAN DEFAULT FALSE,
    email_verified BOOLEAN DEFAULT FALSE,
    image_url VARCHAR(255),
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE SET NULL
);

CREATE TABLE student (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    penalty BOOLEAN DEFAULT FALSE,
    carnet BIGINT UNIQUE NOT NULL,
    degree_id UUID,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE,
    FOREIGN KEY (degree_id) REFERENCES degree(id) ON DELETE SET NULL
);

CREATE TABLE librarian (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL,
    hire_date DATE,
    FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE TABLE loan (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL,
    student_id UUID NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

CREATE TABLE paid_fines (  
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    loan_id UUID UNIQUE NOT NULL,
    amount DECIMAL(10,2) NOT NULL, 
    paid_date DATE NOT NULL DEFAULT CURRENT_DATE, 
    FOREIGN KEY (loan_id) REFERENCES loan(id) ON DELETE CASCADE
);

CREATE TABLE reservation (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL,
    student_id UUID NOT NULL,
    reservation_date DATE NOT NULL,
    expired BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

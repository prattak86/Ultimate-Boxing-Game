# Ultimate-Boxing-Game

Ultimate Boxing game similar to "Mike Tyson's Punch Out" - Now with a modern web-based UI!

![Start Screen](https://github.com/user-attachments/assets/8c504d8b-55f6-4e05-b605-f52503ea2e86)
![Gameplay](https://github.com/user-attachments/assets/beb4cd82-c366-4433-ac86-c8f3d123a6d8)
![Action](https://github.com/user-attachments/assets/591d21a8-5be7-4afa-8241-613b653fd89c)

## 🎮 Features

- **Web-Based UI**: Play directly in your browser with HTML5 Canvas rendering
- **Modern Stack**: Built with Spring Boot 3.2.1 and Java 17
- **Responsive Design**: Works on desktop and mobile devices
- **Real-time Gameplay**: RESTful API with dynamic game state updates
- **Original Assets**: All original graphics and sounds from the Greenfoot version
- **Docker Support**: Containerized for easy deployment
- **CI/CD Pipeline**: Automated builds and publishing to GitHub Packages

## 🎯 Game Controls

- **'A' key** or **Left Punch button**: Throw a left punch
- **'S' key** or **Block button**: Block incoming attacks
- **'D' key** or **Right Punch button**: Throw a right punch

## 🚀 Quick Start

### Option 1: Run with Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/prattak86/Ultimate-Boxing-Game.git
cd Ultimate-Boxing-Game

# Run with Docker Compose
docker-compose up

# Access the game at http://localhost:8080
```

### Option 2: Run with Java

**Prerequisites:**
- Java 17 or higher
- Maven 3.6+

```bash
# Clone the repository
git clone https://github.com/prattak86/Ultimate-Boxing-Game.git
cd Ultimate-Boxing-Game

# Build the project
mvn clean package

# Run the application
java -jar target/ultimate-boxing-game-2.0.0.jar

# Access the game at http://localhost:8080
```

### Option 3: Run with Maven

```bash
# Clone the repository
git clone https://github.com/prattak86/Ultimate-Boxing-Game.git
cd Ultimate-Boxing-Game

# Run with Maven
mvn spring-boot:run

# Access the game at http://localhost:8080
```

## 🏗️ Build from Source

```bash
# Clone the repository
git clone https://github.com/prattak86/Ultimate-Boxing-Game.git
cd Ultimate-Boxing-Game

# Build the JAR file
mvn clean package

# The JAR will be created in target/ultimate-boxing-game-2.0.0.jar
```

## 🐳 Docker

### Build Docker Image

```bash
docker build -t ultimate-boxing-game .
```

### Run Docker Container

```bash
docker run -p 8080:8080 ultimate-boxing-game
```

### Pull from GitHub Container Registry

```bash
docker pull ghcr.io/prattak86/ultimate-boxing-game:latest
docker run -p 8080:8080 ghcr.io/prattak86/ultimate-boxing-game:latest
```

## 📦 Technology Stack

- **Backend**: Spring Boot 3.2.1
- **Java Version**: Java 17 (Eclipse Temurin)
- **Frontend**: HTML5, CSS3, JavaScript
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Container**: Docker & Docker Compose
- **CI/CD**: GitHub Actions

## 🎨 Architecture

The application follows a modern web architecture:

```
├── Backend (Spring Boot)
│   ├── REST API (/api/game/*)
│   ├── Game Service (Game logic)
│   ├── Models (GameState, FighterState)
│   └── Controllers (REST + Web)
├── Frontend (HTML/CSS/JS)
│   ├── HTML5 Canvas rendering
│   ├── Responsive design
│   └── Keyboard/button controls
└── Resources
    ├── Images (PNG sprites)
    └── Sounds (MP3 audio)
```

## 🔧 Development

### Project Structure

```
Ultimate-Boxing-Game/
├── src/
│   └── main/
│       ├── java/com/ultimateboxing/
│       │   ├── BoxingGameApplication.java
│       │   ├── controller/
│       │   ├── service/
│       │   └── model/
│       └── resources/
│           ├── static/
│           │   ├── css/
│           │   ├── js/
│           │   ├── images/
│           │   └── sounds/
│           ├── templates/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package -Pprod
```

## 📝 API Endpoints

- `GET /` - Main game page
- `GET /api/game/state` - Get current game state
- `POST /api/game/action` - Perform player action (leftPunch, rightPunch, block)
- `POST /api/game/reset` - Reset the game

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📜 License

This project maintains the same license as the original repository.

## 👏 Credits

- **Original Game**: Adam Pratt and Ben Slater (Greenfoot version)
- **Web Modernization**: Converted to Spring Boot web application
- **Graphics & Sounds**: Original assets from the Greenfoot version

## 📚 History

This project was originally built using Greenfoot, a Java-based educational framework. It has been modernized to:
- Use the latest Java 17 LTS version
- Run as a web application accessible via browser
- Include Docker support for easy deployment
- Implement CI/CD with GitHub Actions
- Publish artifacts to GitHub Packages

The original Greenfoot version is preserved in the `BoxingRing/` directory for reference.

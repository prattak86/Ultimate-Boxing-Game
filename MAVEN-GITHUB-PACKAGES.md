# Maven GitHub Packages Configuration

If you want to publish to GitHub Packages or pull artifacts from it, you need to configure your Maven settings.

## Setup Instructions

1. Create or edit `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
  http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

2. Generate a GitHub Personal Access Token:
   - Go to GitHub Settings → Developer settings → Personal access tokens
   - Click "Generate new token (classic)"
   - Select scopes: `write:packages`, `read:packages`
   - Copy the token

3. Replace `YOUR_GITHUB_USERNAME` and `YOUR_GITHUB_TOKEN` in settings.xml

## Publishing to GitHub Packages

```bash
mvn clean deploy
```

## Pulling from GitHub Packages

Once configured, Maven will automatically use GitHub Packages when specified in pom.xml.

## CI/CD

The GitHub Actions workflow automatically handles authentication using `GITHUB_TOKEN` secret.

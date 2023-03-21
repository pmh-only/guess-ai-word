cd src\main\responses\frontend

where /q pnpm
IF ERRORLEVEL 0 (
    pnpm install
    pnpm build
    exit /b 0
)

where /q npm
IF ERRORLEVEL 0 (
    npm install
    npm run build
    exit /b 0
)

echo NPM or pNPM not found. please install node.js LTS
exit /b 1
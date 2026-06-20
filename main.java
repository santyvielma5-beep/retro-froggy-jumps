<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Micro Frogger Retro 🐸</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            background: #0f0f0f;
            color: #fff;
            font-family: monospace;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }
        .info { font-size: 11px; margin-bottom: 5px; letter-spacing: 1px; }
        canvas {
            background: #1a1a1a;
            border: 3px solid #4caf50;
            image-rendering: pixelated;
        }
    </style>
</head>
<body>

    <div class="info">NIVEL:<span id="lvl">1</span> | VIDAS:<span id="lives">3</span></div>
    <canvas id="microFrogger" width="210" height="280"></canvas>

    <script>
        const canvas = document.getElementById("microFrogger");
        const ctx = canvas.getContext("2d");
        const lvlEl = document.getElementById("lvl");
        const livesEl = document.getElementById("lives");

        const grid = 28; // Cuadrícula reducida al mínimo funcional
        let level = 1;
        let lives = 3;

        const frog = { x: 3 * grid, y: 9 * grid };

        // Tráfico adaptado al tamaño micro
        let cars = [
            { x: 0, y: 8 * grid, speed: 1.2, len: 35, color: '#ff5722' },
            { x: 90, y: 8 * grid, speed: 1.2, len: 35, color: '#ff5722' },
            { x: 30, y: 7 * grid, speed: -1.6, len: 30, color: '#ffeb3b' },
            { x: 140, y: 7 * grid, speed: -1.6, len: 30, color: '#ffeb3b' },
            { x: 0, y: 6 * grid, speed: 2.0, len: 45, color: '#00bcd4' },
            { x: 80, y: 4 * grid, speed: -1.4, len: 32, color: '#e91e63' },
            { x: 10, y: 3 * grid, speed: 2.2, len: 35, color: '#9c27b0' }
        ];

        function resetFrog() {
            frog.x = 3 * grid;
            frog.y = 9 * grid;
        }

        window.addEventListener("keydown", (e) => {
            if (e.key === "ArrowUp" || e.key === "w" || e.key === "W") frog.y -= grid;
            if (e.key === "ArrowDown" || e.key === "s" || e.key === "S") frog.y += grid;
            if (e.key === "ArrowLeft" || e.key === "a" || e.key === "A") frog.x -= grid;
            if (e.key === "ArrowRight" || e.key === "d" || e.key === "D") frog.x += grid;

            if (frog.x < 0) frog.x = 0;
            if (frog.x >= canvas.width) frog.x = canvas.width - grid;
            if (frog.y >= canvas.height) frog.y = canvas.height - grid;

            if (frog.y <= 0) {
                level++;
                lvlEl.innerText = level;
                cars.forEach(c => c.speed *= 1.2);
                resetFrog();
            }
        });

        function update() {
            cars.forEach(car => {
                car.x += car.speed;
                if (car.speed > 0 && car.x > canvas.width) car.x = -car.len;
                if (car.speed < 0 && car.x < -car.len) car.x = canvas.width;

                if (frog.y === car.y && frog.x + 4 < car.x + car.len && frog.x + grid - 4 > car.x) {
                    lives--;
                    livesEl.innerText = lives;
                    
                    if (lives <= 0) {
                        alert("GAME OVER 💀");
                        level = 1; lives = 3;
                        lvlEl.innerText = level; livesEl.innerText = lives;
                        cars.forEach(c => c.speed = Math.sign(c.speed) * Math.abs(c.speed / Math.pow(1.2, level)));
                    }
                    resetFrog();
                }
            });
        }

        function draw() {
            ctx.clearRect(0, 0, canvas.width, canvas.height);

            // Zonas Seguras
            ctx.fillStyle = "#144216"; 
            ctx.fillRect(0, 0, canvas.width, grid);
            ctx.fillRect(0, 5 * grid, canvas.width, grid); 
            ctx.fillRect(0, 9 * grid, canvas.width, grid); 

            // Carriles
            ctx.strokeStyle = "rgba(255,255,255,0.08)";
            ctx.lineWidth = 1;
            for(let i = 2; i <= 8; i++) {
                if(i !== 5 && i !== 9) {
                    ctx.beginPath(); ctx.moveTo(0, i * grid); ctx.lineTo(canvas.width, i * grid); ctx.stroke();
                }
            }

            // Autos Micro
            cars.forEach(car => {
                ctx.fillStyle = car.color;
                ctx.fillRect(car.x, car.y + 4, car.len, grid - 8);
                ctx.fillStyle = "#000";
                ctx.fillRect(car.x + 2, car.y + 1, 5, 3);
                ctx.fillRect(car.x + car.len - 7, car.y + 1, 5, 3);
                ctx.fillRect(car.x + 2, car.y + grid - 4, 5, 3);
                ctx.fillRect(car.x + car.len - 7, car.y + grid - 4, 5, 3);
            });

            // Ranita Micro
            ctx.fillStyle = "#4caf50";
            ctx.fillRect(frog.x + 4, frog.y + 4, grid - 8, grid - 8);
            ctx.fillStyle = "#fff";
            ctx.fillRect(frog.x + 6, frog.y + 6, 3, 3);
            ctx.fillRect(frog.x + grid - 9, frog.y + 6, 3, 3);
        }

        function loop() { update(); draw(); requestAnimationFrame(loop); }
        loop();
    </script>
</body>
</html>

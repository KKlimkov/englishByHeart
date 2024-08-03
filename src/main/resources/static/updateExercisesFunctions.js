async function updateExercises() {
        try {
            const exercisesResponse = await fetch('/updateExercises?userId=1&mode=SENTENCE', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            const exercises = await exercisesResponse.json();
            // Handle exercises as needed
        } catch (error) {
            console.error('Error fetching exercises:', error);
        }
    }
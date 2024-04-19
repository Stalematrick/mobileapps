package ru.abramovkirill.languageapp.exercises.guess_animal.impl.mappers

import ru.abramovkirill.languageapp.exercises.guess_animal.impl.data.GuessAnimalExerciseData
import ru.abramovkirill.languageapp.exercises.guess_animal.impl.domain.GuessAnimalExercise

fun GuessAnimalExerciseData.toDomain() =
    GuessAnimalExercise(
        id = id,
        imageUrl = imageUrl,
    )

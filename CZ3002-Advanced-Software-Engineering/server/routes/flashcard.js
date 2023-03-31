import express from 'express';
import { createOneFlashcard } from '../controllers/flashcards/create.js';
import { getBaseFolderFlashcards, getFlashcardsCramMode,  getFlashcardsStudyMode } from '../controllers/flashcards/read.js';
import { updateFlashcard, rescheduleFlashcard } from '../controllers/flashcards/update.js';
import { deleteFlashcard } from '../controllers/flashcards/delete.js';
import { exportFlashcards } from '../controllers/flashcards/export.js';
import { importFlashcards } from '../controllers/flashcards/import.js';

var router = express.Router();

// Post requests
router.post('/add-flashcard/:id', createOneFlashcard);
router.post('/import-flashcards/:id', importFlashcards);
router.post('/reschedule-flashcard/:id', rescheduleFlashcard);

// Get requests
router.get('/get-flashcards', getBaseFolderFlashcards);
router.get('/study-mode', getFlashcardsStudyMode);
router.get('/cram-mode', getFlashcardsCramMode);
router.get('/export-flashcards/:id', exportFlashcards);

// Put requests
router.put('/update-flashcard', updateFlashcard);
router.put('/reschedule-flashcard/:id', rescheduleFlashcard);

// Delete requests
router.delete('/delete-flashcard/:id', deleteFlashcard);

export default router;
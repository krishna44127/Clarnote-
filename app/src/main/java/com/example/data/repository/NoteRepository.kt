package com.example.data.repository

import com.example.data.db.NoteDao
import com.example.data.model.Flashcard
import com.example.data.model.FormulaItem
import com.example.data.model.NoteItem
import com.example.data.model.NoteType
import com.example.data.model.UploadStatus
import com.example.data.model.VideoBookmark
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

class NoteRepository(private val noteDao: NoteDao) {

    val activeNotes: Flow<List<NoteItem>> = noteDao.getActiveNotes()
    val starredNotes: Flow<List<NoteItem>> = noteDao.getStarredNotes()
    val mistakeDiaryNotes: Flow<List<NoteItem>> = noteDao.getMistakeDiaryNotes()
    val vaultNotes: Flow<List<NoteItem>> = noteDao.getVaultNotes()
    val trashNotes: Flow<List<NoteItem>> = noteDao.getTrashNotes()

    suspend fun insertNote(note: NoteItem): Long = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteItem) = noteDao.updateNote(note)

    suspend fun moveToTrash(id: Long) = noteDao.moveToTrash(id, System.currentTimeMillis())

    suspend fun restoreFromTrash(id: Long) = noteDao.restoreFromTrash(id)

    suspend fun deletePermanently(id: Long) = noteDao.deletePermanently(id)

    suspend fun emptyTrash() = noteDao.emptyTrash()

    suspend fun getNoteByIdSync(id: Long): NoteItem? = noteDao.getNoteByIdSync(id)

    fun computeSha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

    suspend fun checkDuplicate(hash: String): NoteItem? {
        val duplicates = noteDao.findBySha256(hash)
        return duplicates.firstOrNull()
    }

    suspend fun checkNearDuplicate(title: String, content: String): NoteItem? {
        val active = noteDao.getAllActiveList()
        val contentWords = (title + " " + content).lowercase().split("\\s+".toRegex()).filter { it.length > 3 }.toSet()
        if (contentWords.isEmpty()) return null

        for (existing in active) {
            val existingWords = (existing.title + " " + existing.content + " " + existing.ocrText).lowercase().split("\\s+".toRegex()).filter { it.length > 3 }.toSet()
            if (existingWords.isEmpty()) continue
            val intersection = contentWords.intersect(existingWords)
            val similarity = (intersection.size * 2.0) / (contentWords.size + existingWords.size)
            if (similarity > 0.65 && existing.sha256Hash != computeSha256(content)) {
                return existing
            }
        }
        return null
    }

    fun autoCategorizeSubject(title: String, content: String): String {
        val text = (title + " " + content).lowercase()
        val physicsKeywords = listOf("optics", "ray", "snell", "lens", "mirror", "light", "refraction", "mechanics", "velocity", "friction", "gravity", "thermodynamics", "entropy", "electrostatics", "coulomb", "circuit", "current", "magnetic", "faraday", "quantum", "wave", "bohr", "photoelectric")
        val chemistryKeywords = listOf("mole", "stoichiometry", "organic", "alkane", "alkene", "benzene", "alcohol", "reaction", "reagent", "orbital", "hybridization", "periodic", "acid", "base", "ph", "equilibrium", "kinetics", "enthalpy", "redox", "titration", "bond")
        val biologyKeywords = listOf("cell", "mitosis", "meiosis", "genetics", "dna", "rna", "mendel", "chromosome", "photosynthesis", "respiration", "neuron", "synapse", "cardiovascular", "heart", "kidney", "enzyme", "bacteria", "chloroplast", "ecology")
        val mathKeywords = listOf("calculus", "derivative", "integral", "integration", "limit", "matrix", "matrices", "determinant", "vector", "geometry", "probability", "permutation", "trigonometry", "algebra", "quadratic")
        val csKeywords = listOf("algorithm", "complexity", "binary", "tree", "graph", "sorting", "hashmap", "recursion", "dynamic programming", "python", "kotlin", "java", "sql", "database")

        val pScore = physicsKeywords.count { text.contains(it) }
        val cScore = chemistryKeywords.count { text.contains(it) }
        val bScore = biologyKeywords.count { text.contains(it) }
        val mScore = mathKeywords.count { text.contains(it) }
        val csScore = csKeywords.count { text.contains(it) }

        val maxScore = maxOf(pScore, cScore, bScore, mScore, csScore)
        if (maxScore == 0) return "General"

        return when (maxScore) {
            pScore -> "Physics"
            cScore -> "Chemistry"
            bScore -> "Biology"
            mScore -> "Mathematics"
            else -> "Computer Science"
        }
    }

    fun extractTopicTags(title: String, content: String): String {
        val text = (title + " " + content).lowercase()
        val tagList = mutableListOf<String>()

        if (text.contains("optic") || text.contains("lens") || text.contains("refraction")) tagList.add("#Optics")
        if (text.contains("formula") || text.contains("equation") || text.contains("deriv")) tagList.add("#Formulas")
        if (text.contains("pyq") || text.contains("question") || text.contains("exam") || text.contains("doubt")) tagList.add("#PyQ")
        if (text.contains("thermo") || text.contains("heat") || text.contains("carnot")) tagList.add("#Thermodynamics")
        if (text.contains("organic") || text.contains("benzene") || text.contains("mechanism")) tagList.add("#Organic")
        if (text.contains("cell") || text.contains("gene") || text.contains("dna")) tagList.add("#Genetics")
        if (text.contains("deriv") || text.contains("integral") || text.contains("calculus")) tagList.add("#Calculus")
        if (text.contains("revision") || text.contains("important") || text.contains("key")) tagList.add("#KeyNotes")

        if (tagList.isEmpty()) {
            tagList.add("#Notes")
            tagList.add("#ExamPrep")
        }
        return tagList.joinToString(", ")
    }

    fun generateAutoFlashcards(title: String, content: String): List<Flashcard> {
        val cards = mutableListOf<Flashcard>()
        val sentences = content.split(".").map { it.trim() }.filter { it.length > 20 }

        if (sentences.isNotEmpty()) {
            for (i in 0 until minOf(4, sentences.size)) {
                val s = sentences[i]
                if (s.contains("is", ignoreCase = true) || s.contains("states", ignoreCase = true)) {
                    val parts = s.split(" is ", " states that ", ignoreCase = true)
                    if (parts.size == 2) {
                        cards.add(Flashcard("What is ${parts[0].trim()}?", parts[1].trim() + "."))
                        continue
                    }
                }
                cards.add(Flashcard("Key concept from $title (Part ${i + 1}):", s + "."))
            }
        }

        if (cards.isEmpty()) {
            cards.add(Flashcard("What is the main topic of $title?", "Detailed summary and essential definitions covered in this study note."))
            cards.add(Flashcard("Why is this topic important for exams?", "High frequency questions and foundational formulas often appear in competitive tests."))
        }
        return cards
    }

    fun generateExtractedFormulas(title: String, content: String): List<FormulaItem> {
        val list = mutableListOf<FormulaItem>()
        val lower = content.lowercase()
        if (lower.contains("snell") || lower.contains("refraction") || lower.contains("optic")) {
            list.add(FormulaItem("Snell's Law", "n₁ sin(θ₁) = n₂ sin(θ₂)", "Relates angle of incidence to angle of refraction across mediums."))
            list.add(FormulaItem("Lens Maker's Formula", "1/f = (μ - 1)(1/R₁ - 1/R₂)", "Focal length determination based on curvature radii."))
        }
        if (lower.contains("thermo") || lower.contains("heat") || lower.contains("work")) {
            list.add(FormulaItem("First Law of Thermodynamics", "ΔU = Q - W", "Change in internal energy equals heat supplied minus work done."))
            list.add(FormulaItem("Ideal Gas Law", "P · V = n · R · T", "Equation of state for an ideal gas."))
        }
        if (lower.contains("kinetics") || lower.contains("reaction") || lower.contains("rate")) {
            list.add(FormulaItem("Arrhenius Equation", "k = A · e^(-Ea / RT)", "Temperature dependence of reaction rate constants."))
            list.add(FormulaItem("First Order Half-Life", "t½ = 0.693 / k", "Time required for half of the reactant concentration to decay."))
        }
        if (lower.contains("calculus") || lower.contains("integral") || lower.contains("derivative")) {
            list.add(FormulaItem("Integration by Parts", "∫ u dv = u·v - ∫ v du", "Technique for integrating the product of two functions."))
            list.add(FormulaItem("Euler's Identity", "e^(iπ) + 1 = 0", "Fundamental link between algebra, geometry, and analysis."))
        }

        if (list.isEmpty()) {
            list.add(FormulaItem("Core Equation ($title)", "F_net = m · a", "Fundamental relation governing system dynamics."))
            list.add(FormulaItem("Conservation Law", "E_total = E_potential + E_kinetic", "Total mechanical energy remains constant in an isolated conservative system."))
        }
        return list
    }

    suspend fun clearAllData() {
        val all = noteDao.getAllActiveList()
        for (item in all) {
            noteDao.deletePermanently(item.id)
        }
        noteDao.emptyTrash()
    }
}

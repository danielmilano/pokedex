package it.danielmilano.pokedex

import android.content.Context
import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import it.danielmilano.pokedex.database.AppDatabase
import it.danielmilano.pokedex.database.dao.PokemonDAO
import it.danielmilano.pokedex.pokemon.model.Pokemon
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.pokemon.ui.detail.PokemonDetailViewModel
import it.danielmilano.pokedex.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@ExperimentalCoroutinesApi
@Config(maxSdk = Build.VERSION_CODES.P, minSdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner::class)
class PokemonDetailViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val getPokemonDetailUseCase = Mockito.mock(GetPokemonDetailUseCase::class.java)

    private lateinit var pokemonDAO: PokemonDAO

    private lateinit var db: AppDatabase

    private lateinit var viewModel: PokemonDetailViewModel

    private val pokemonListItem = PokemonListItem(
        "ditto",
        "https://pokeapi.co/api/v2/pokemon/ditto",
        "https://pokeapi.co/api/v2/pokemon?offset=300&limit=100"
    )
    private val pokemon = Pokemon("132", 101, 3, "ditto", 40)

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        pokemonDAO = db.pokemonDAO()
        viewModel = PokemonDetailViewModel(pokemonDAO, getPokemonDetailUseCase)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = runBlocking {
        this.launch {
            withContext(Dispatchers.IO) {
                pokemonDAO.deleteAll()
                db.close()
            }
        }
        return@runBlocking
    }

    @Test
    fun getPokemonDetailAlreadySaved() = testDispatcher.runBlockingTest {
        testScope.launch {
            withContext(Dispatchers.IO) {
                pokemonDAO.add(pokemon)
                viewModel.getPokemonDetail(pokemonListItem.name, pokemonListItem.url)
                Mockito.verify(getPokemonDetailUseCase, Mockito.times(0))
                    .invoke(pokemonListItem.url)
            }
        }
    }

    @Test
    fun getPokemonDetailFirstTime()  = testDispatcher.runBlockingTest {
        testScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.getPokemonDetail(pokemonListItem.name, pokemonListItem.url)
                Mockito.verify(getPokemonDetailUseCase, Mockito.times(1))
                    .invoke(pokemonListItem.url)
            }
        }
    }
}
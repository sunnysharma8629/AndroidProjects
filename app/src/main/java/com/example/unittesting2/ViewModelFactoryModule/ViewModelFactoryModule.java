package com.example.unittesting2.ViewModelFactoryModule;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.unittesting2.DAOPackage.DataViewModel;
import com.example.unittesting2.DAOPackage.NotelistViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

    @Binds
    @IntoMap
    @ViewModelKeyProvider(DataViewModel.class)
    public abstract ViewModel bindNoteViewModel(DataViewModel noteViewModel);

    @Binds
    @IntoMap
    @ViewModelKeyProvider(NotelistViewModel.class)
    public abstract ViewModel bindNotesListViewModel(NotelistViewModel noteViewModel);

}

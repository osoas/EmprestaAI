import React, { useState } from 'react';
import { ScreenName, Item, Rental } from './types';
import { LoginScreen, RegisterScreen, AddressScreen } from './screens/AuthScreens';
import { HomeScreen, AddItemScreen, ProfileScreen, ItemDetailScreen, RentalStatusScreen, RentalListScreen, EditItemScreen } from './screens/MainScreens';
import { BottomNav, ToastContainer } from './components/UI';

const App: React.FC = () => {
  const [currentScreen, setCurrentScreen] = useState<ScreenName>('login');
  const [selectedItem, setSelectedItem] = useState<Item | null>(null);
  const [itemToEdit, setItemToEdit] = useState<Item | null>(null);
  const [selectedRental, setSelectedRental] = useState<Rental | null>(null);
  const [user, setUser] = useState<any>(null);
  const [tempRegisterData, setTempRegisterData] = useState<any>({});

  const navigate = (screen: ScreenName) => {
    setCurrentScreen(screen);
    window.scrollTo(0, 0);
  };

  const handleItemClick = (item: Item) => {
    setSelectedItem(item);
    navigate('detail');
  };

  const handleEditItem = (item: Item) => {
    setItemToEdit(item);
    navigate('edit-item');
  };

  const handleRentalClick = (rental: Rental) => {
    setSelectedRental(rental);
    navigate('rental-status');
  };

  const renderScreen = () => {
    switch (currentScreen) {
      case 'login':
        return <LoginScreen onNavigate={navigate} onLogin={setUser} />;
      case 'register':
        return <RegisterScreen onNavigate={navigate} onUpdateTempData={setTempRegisterData} tempData={tempRegisterData} />;
      case 'address':
        return <AddressScreen onNavigate={navigate} tempData={tempRegisterData} onRegister={setUser} />;
      case 'home':
        return <HomeScreen onItemClick={handleItemClick} />;
      case 'add':
        return <AddItemScreen onSave={() => navigate('profile')} user={user} />;
      case 'edit-item':
        return itemToEdit ? (
          <EditItemScreen item={itemToEdit} onSave={() => { setItemToEdit(null); navigate('profile'); }} onBack={() => { setItemToEdit(null); navigate('profile'); }} />
        ) : <ProfileScreen onNavigate={navigate} onEditItem={handleEditItem} user={user} />;
      case 'profile':
        return <ProfileScreen onNavigate={navigate} onEditItem={handleEditItem} user={user} />;
      case 'detail':
        return selectedItem ? (
          <ItemDetailScreen item={selectedItem} onBack={() => navigate('home')} user={user} />
        ) : (
          <HomeScreen onItemClick={handleItemClick} />
        );
      case 'rental-list':
        return <RentalListScreen onBack={() => navigate('profile')} onRentalClick={handleRentalClick} user={user} />;
      case 'rental-status':
        return <RentalStatusScreen onBack={() => { setSelectedRental(null); navigate('rental-list'); }} rental={selectedRental || undefined} user={user} />;
      default:
        return <LoginScreen onNavigate={navigate} onLogin={setUser} />;
    }
  };

  const showBottomNav = ['home', 'add', 'profile'].includes(currentScreen);

  return (
    <div className="min-h-screen bg-gray-200 flex justify-center">
      <div className="w-full max-w-md bg-white min-h-screen shadow-2xl relative overflow-hidden flex flex-col">
        <main className="flex-1 relative overflow-y-auto no-scrollbar overflow-x-hidden">
          {renderScreen()}
        </main>

        {showBottomNav && (
          <BottomNav active={currentScreen} onNavigate={navigate} />
        )}
        <ToastContainer />
      </div>
    </div>
  );
};

export default App;
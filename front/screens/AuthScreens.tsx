import React, { useState } from 'react';
import { HeaderWave, Input, Button, showToast } from '../components/UI';
import { Icons } from '../components/Icons';
import { ScreenName } from '../types';
import api from '@/src/services/api';
import { AppLogo } from '../components/AppLogo';
interface AuthProps {
  onNavigate: (screen: ScreenName) => void;
  onLogin?: (user: any) => void;
  onUpdateTempData?: (data: any) => void;
  tempData?: any;
  onRegister?: (user: any) => void;
}

export const LoginScreen: React.FC<AuthProps> = ({ onNavigate, onLogin }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLogin = async () => {
    setLoading(true);
    setError('');
    try {
      const response = await api.post('/auth/login', { email, senha: password });
      if (response && response.data) {
        if (onLogin) onLogin(response.data);
        showToast('Login realizado com sucesso', 'success');
        onNavigate('home');
      } else {
        setError('Credenciais inválidas');
        showToast('Credenciais inválidas', 'error');
      }
    } catch (err) {
      setError('Erro ao fazer login');
      console.error(err);
      showToast('Erro ao fazer login', 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-white flex flex-col">
      <AppLogo className="w-full h-auto" title="Bem vindo de volta!" />

      <div className="flex-1 px-6 pt-8 flex flex-col gap-4">
        <Input
          placeholder="Seu Email"
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <Input
          placeholder="Insira sua senha"
          label="Senha"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        {error && <p className="text-red-500 text-sm text-center">{error}</p>}

        <div className="mt-8">
          <Button onClick={handleLogin} loading={loading}>
            {loading ? 'Entrando...' : 'Entrar'}
          </Button>
        </div>

        <p className="text-center text-sm mt-4 text-gray-500">
          Não tem conta? <button onClick={() => onNavigate('register')} className="text-black font-bold underline">Registre-se</button>
        </p>
      </div>
      <HeaderWave showLogo={true} />

    </div>
  );
};

export const RegisterScreen: React.FC<AuthProps> = ({ onNavigate, onUpdateTempData, tempData }) => {
  const [formData, setFormData] = useState({
    nome: tempData?.nome || '',
    email: tempData?.email || '',
    cpf: tempData?.cpf || '',
    senha: tempData?.senha || '',
  });

  const handleChange = (field: string, value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  const handleNext = () => {
    if (onUpdateTempData) onUpdateTempData(formData);
    onNavigate('address');
  };

  return (
    <div className="min-h-screen bg-white flex flex-col">
      <AppLogo className="w-full h-auto" title="Cadastre-se e empresta aí!"/>

      <div className="flex-1 px-6 pt-4 flex flex-col gap-4 overflow-y-auto pb-8">
        <Input
          placeholder="Seu Nome"
          label="Nome"
          value={formData.nome}
          onChange={(e) => handleChange('nome', e.target.value)}
        />
        <Input
          placeholder="Seu Email"
          label="Email"
          type="email"
          value={formData.email}
          onChange={(e) => handleChange('email', e.target.value)}
        />
        <Input
          placeholder="Insira seu CPF"
          label="CPF"
          value={formData.cpf}
          onChange={(e) => handleChange('cpf', e.target.value)}
        />

        <div className="relative pointer-events-none opacity-50">
          <Input placeholder="Informe seu endereço" label="Endereço" />
          <Icons.MapPin className="absolute right-4 top-10 text-gray-500 w-5 h-5" />
        </div>

        <Input
          placeholder="Crie sua senha!"
          label="Senha"
          type="password"
          value={formData.senha}
          onChange={(e) => handleChange('senha', e.target.value)}
        />

        <div className="mt-4 pb-8">
          <Button onClick={handleNext}>Continuar</Button>
        </div>
      </div>
      <HeaderWave  showLogo={true} showBack onBack={() => onNavigate('login')} />

    </div>
  );
};

export const AddressScreen: React.FC<AuthProps> = ({ onNavigate, tempData, onRegister }) => {
  const [address, setAddress] = useState({
    cep: '',
    estado: '',
    cidade: '',
    municipio: '',
    bairro: '',
    rua: '',
    numeroDaCasa: '',
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (field: string, value: string) => {
    setAddress(prev => ({ ...prev, [field]: value }));
  };

  const handleRegister = async () => {
    setLoading(true);
    try {
      const fullUser = {
        ...tempData,
        enderecos: [{
          ...address,
          numeroDaCasa: parseInt(address.numeroDaCasa) || 0
        }]
      };

      const response = await api.post('/user', fullUser);
      if (response.status === 201 || response.status === 200) {
        if (onRegister) onRegister(response.data);
        onNavigate('home');
      }
    } catch (err) {
      console.error(err);
      alert('Erro ao registrar');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-white flex flex-col">
      <AppLogo className="w-full h-auto" title="Quase lá, insira seu endereço"/>

      <div className="flex-1 px-6 pt-4 flex flex-col gap-3 overflow-y-auto pb-8">
        <Input
          placeholder="Insira seu CEP"
          label="CEP"
          value={address.cep}
          onChange={(e) => handleChange('cep', e.target.value)}
        />
        <Input
          placeholder="Insira o estado"
          label="Estado"
          value={address.estado}
          onChange={(e) => handleChange('estado', e.target.value)}
        />
        <Input
          placeholder="Insira a sua cidade"
          label="Cidade"
          value={address.cidade}
          onChange={(e) => handleChange('cidade', e.target.value)}
        />
        <Input
          placeholder="Insira seu munícipio"
          label="Município"
          value={address.municipio}
          onChange={(e) => handleChange('municipio', e.target.value)}
        />
        <Input
          placeholder="Insira seu bairro"
          label="Bairro"
          value={address.bairro}
          onChange={(e) => handleChange('bairro', e.target.value)}
        />
        <Input
          placeholder="Insira sua rua"
          label="Rua"
          value={address.rua}
          onChange={(e) => handleChange('rua', e.target.value)}
        />
        <Input
          placeholder="Insira o número de sua casa"
          label="Número"
          value={address.numeroDaCasa}
          onChange={(e) => handleChange('numeroDaCasa', e.target.value)}
        />

        <div className="mt-6 pb-8">
          <Button onClick={handleRegister} disabled={loading}>
            {loading ? 'Registrando...' : 'Registrar endereço'}
          </Button>
        </div>
      </div>
      <HeaderWave title="Insira seu endereço" showBack onBack={() => onNavigate('register')} />

    </div>
  );
};

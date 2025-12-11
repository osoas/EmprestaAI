import React, { useState, useEffect } from 'react';
import { Input, Button, ItemCard, showToast } from '../components/UI';
import { Icons } from '../components/Icons';
import { Item, ScreenName, Rental } from '../types';
import api from '@/src/services/api';

const FALLBACK_RENTAL: Rental = {
    id: 'r0',
    itemId: '0',
    itemName: 'Item genérico',
    itemImage: 'https://picsum.photos/300/300?random=0',
    status: 'returned',
    startDate: new Date().toISOString(),
    endDate: new Date().toISOString(),
    totalPrice: 0
};

export const HomeScreen: React.FC<{ onItemClick: (item: Item) => void }> = ({ onItemClick }) => {
    const [search, setSearch] = useState('');
    const [items, setItems] = useState<Item[]>([]);
    const [loading, setLoading] = useState(true);

    const [selectedCategory, setSelectedCategory] = useState<string>('Todos');
    const [categories, setCategories] = useState<any[]>([]);

    useEffect(() => {
        fetchItems();
        fetchCategories();
    }, []);

    const fetchCategories = async () => {
        try {
            const resp = await api.get('/categoria/list');
            setCategories(resp.data);
        } catch (err) {
            console.error('Erro ao buscar categorias', err);
        }
    };

    const fetchItems = async () => {
        try {
            const response = await api.get('/item/list/stats');
            const mappedItems = response.data.map((i: any) => ({
                id: i.id?.toString(),
                name: i.nome_item,
                description: i.descricao,
                price: parseFloat(i.valor_unitario),
                image: 'https://picsum.photos/300/300?random=' + i.id,
                ratingAvg: i.ratingAvg,
                ratingCount: i.ratingCount,
                type: i.categoria_item?.nome_categoria || 'Geral',
                ownerId: i.proprietarioId?.toString()
            }));
            setItems(mappedItems);
        } catch (err) {
            console.error('Erro ao buscar itens', err);
            showToast('Erro ao buscar itens', 'error');
        } finally {
            setLoading(false);
        }
    };

    const filteredItems = items.filter(i => {
        const matchesSearch = i.name.toLowerCase().includes(search.toLowerCase());
        const matchesCategory = selectedCategory === 'Todos' || i.type === selectedCategory;
        return matchesSearch && matchesCategory;
    });

    return (
        <div className="flex flex-col h-full bg-gray-50 animate-fade-in">
            <div className="p-6 bg-white pb-4 sticky top-0 z-10 shadow-sm">
                <div className="relative">
                    <Icons.Search className="absolute left-4 top-3.5 text-gray-400 w-5 h-5" />
                    <input
                        type="text"
                        placeholder="Pesquisar"
                        className="w-full bg-gray-100 rounded-full pl-12 pr-4 py-3 outline-none focus:ring-2 focus:ring-black transition-all"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>
                <div className="flex gap-2 mt-4 overflow-x-auto no-scrollbar pb-2">
                    <button
                        onClick={() => setSelectedCategory('Todos')}
                        className={`px-4 py-1.5 rounded-full border text-xs font-medium whitespace-nowrap transition-colors ${selectedCategory === 'Todos' ? 'bg-black text-white border-black' : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-100'}`}
                    >
                        Todos
                    </button>
                    {categories.map((cat) => (
                        <button
                            key={cat.id}
                            onClick={() => setSelectedCategory(cat.nome_categoria)}
                            className={`px-4 py-1.5 rounded-full border text-xs font-medium whitespace-nowrap transition-colors ${selectedCategory === cat.nome_categoria ? 'bg-black text-white border-black' : 'bg-white text-gray-600 border-gray-200 hover:bg-gray-100'}`}
                        >
                            {cat.nome_categoria}
                        </button>
                    ))}
                </div>
            </div>

            <div className="p-4 grid grid-cols-2 gap-4 pb-24 overflow-y-auto">
                {loading ? <p className="text-center col-span-2">Carregando...</p> :
                    filteredItems.map((item, idx) => (
                        <div key={item.id} style={{ animationDelay: `${idx * 100}ms` }} className="animate-slide-up">
                            <ItemCard item={item} onClick={() => onItemClick(item)} />
                        </div>
                    ))}
                {!loading && filteredItems.length === 0 && <p className="text-center col-span-2 text-gray-500">Nenhum item encontrado.</p>}
            </div>
        </div>
    );
};

export const AddItemScreen: React.FC<{ onSave: () => void, user: any }> = ({ onSave, user }) => {
    const [formData, setFormData] = useState({
        nome_item: '',
        descricao: '',
        valor_unitario: '',
        categoriaId: ''
    });
    const [loading, setLoading] = useState(false);
    const [categories, setCategories] = useState<any[]>([]);

    useEffect(() => {
        api.get('/categoria/list').then(resp => {
            setCategories(resp.data);
            if (resp.data.length > 0) setFormData(prev => ({ ...prev, categoriaId: resp.data[0].id }));
        }).catch(err => console.error('Error fetching categories', err));
    }, []);

    const handleChange = (field: string, value: string) => {
        setFormData(prev => ({ ...prev, [field]: value }));
    };

    const handleSave = async () => {
        if (!user) {
            showToast('Você precisa estar logado para adicionar itens.', 'error');
            return;
        }
        setLoading(true);
        try {
            const payload = {
                nome_item: formData.nome_item,
                descricao: formData.descricao,
                valor_unitario: parseFloat(formData.valor_unitario),
                proprietario: { id: user.id },
                categoria_item: { id: parseInt(formData.categoriaId) }
            };
            await api.post('/item', payload);

            try {
                window.dispatchEvent(new CustomEvent('itemCreated', { detail: payload }));
            } catch (e) {
                const ev = document.createEvent('CustomEvent');
                ev.initCustomEvent('itemCreated', true, true, payload);
                window.dispatchEvent(ev);
            }

            onSave();
        } catch (err) {
            console.error('Erro ao criar item', err);
            showToast('Erro ao criar item', 'error');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex flex-col h-full bg-white">
            <div className="p-6">
                <h2 className="text-2xl font-mono font-bold mb-6">Novo Item</h2>

                <div className="flex flex-col gap-4 overflow-y-auto pb-24 h-full">
                    <div className="w-full h-48 bg-gray-100 rounded-2xl flex flex-col items-center justify-center border-2 border-dashed border-gray-300 text-gray-400">
                        <Icons.Plus className="w-8 h-8 mb-2" />
                        <span className="text-sm">Adicionar fotos (Indisponível)</span>
                    </div>

                    <Input
                        label="Nome do item"
                        placeholder="Insira o nome do item"
                        value={formData.nome_item}
                        onChange={(e) => handleChange('nome_item', e.target.value)}
                    />

                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-1 ml-1">Tipo</label>
                        <div className="relative">
                            <select
                                className="w-full bg-gray-200 text-gray-900 rounded-2xl px-5 py-3 outline-none appearance-none"
                                value={formData.categoriaId}
                                onChange={(e) => handleChange('categoriaId', e.target.value)}
                            >
                                {categories.map(cat => (
                                    <option key={cat.id} value={cat.id}>{cat.nome_categoria}</option>
                                ))}
                            </select>
                            <div className="absolute right-4 top-4 pointer-events-none">
                                <svg className="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7"></path></svg>
                            </div>
                        </div>
                    </div>

                    <Input
                        label="Descrição"
                        placeholder="Descreva mais informações"
                        value={formData.descricao}
                        onChange={(e) => handleChange('descricao', e.target.value)}
                    />
                    <Input
                        label="Preço"
                        placeholder="Preço por dia de aluguel"
                        type="number"
                        value={formData.valor_unitario}
                        onChange={(e) => handleChange('valor_unitario', e.target.value)}
                    />

                    <Button onClick={handleSave} className="mt-4" loading={loading}>
                        {loading ? 'Registrando...' : 'Registrar produto'}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export const EditItemScreen: React.FC<{ item: Item, onSave: () => void, onBack: () => void }> = ({ item, onSave, onBack }) => {
    return (
        <div className="flex flex-col h-full bg-white">
            <div className="flex items-center p-6 gap-4">
                <button onClick={onBack} className="p-2 -ml-2 hover:bg-gray-100 rounded-full"><Icons.Back /></button>
                <h2 className="text-2xl font-mono font-bold">Editar Item</h2>
            </div>

            <div className="p-6 pt-0 flex flex-col gap-4 overflow-y-auto pb-24 h-full">
                <div className="w-full h-48 bg-gray-100 rounded-2xl overflow-hidden relative group cursor-pointer">
                    <img src={item.image} alt={item.name} className="w-full h-full object-cover opacity-100 transition-opacity group-hover:opacity-80" />
                    <div className="absolute inset-0 flex items-center justify-center bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity">
                        <span className="text-white font-bold flex items-center gap-2"><Icons.Edit size={16} /> Alterar foto</span>
                    </div>
                </div>

                <Input label="Nome do item" defaultValue={item.name} />

                <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-1 ml-1">Tipo</label>
                    <div className="relative">
                        <select defaultValue={item.type} className="w-full bg-gray-200 text-gray-900 rounded-2xl px-5 py-3 outline-none appearance-none">
                            <option>Ferramenta</option>
                            <option>Eletrônico</option>
                            <option>Lazer</option>
                            <option>Utensílio</option>
                            <option>Acessório</option>
                        </select>
                        <div className="absolute right-4 top-4 pointer-events-none">
                            <svg className="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 9l-7 7-7-7"></path></svg>
                        </div>
                    </div>
                </div>

                <Input label="Descrição" defaultValue={item.description} />
                <Input label="Preço" defaultValue={item.price.toString()} type="number" />

                <Button onClick={onSave} className="mt-4">Salvar alterações</Button>
            </div>
        </div>
    );
};

export const ProfileScreen: React.FC<{
    onNavigate: (screen: ScreenName) => void;
    onEditItem: (item: Item) => void;
    user?: any;
}> = ({ onNavigate, onEditItem, user }) => {
    const [myItems, setMyItems] = useState<Item[] | null>(null);
    const [solicitacoes, setSolicitacoes] = useState<any[] | null>(null);
    const [isAdmin, setIsAdmin] = useState<boolean>(() => !!localStorage.getItem('isAdmin'));

    const toggleAdmin = () => {
        const next = !isAdmin;
        setIsAdmin(next);
        if (next) localStorage.setItem('isAdmin', '1'); else localStorage.removeItem('isAdmin');
        try { (window as any).__isAdmin = next; } catch (e) { }
        try { window.dispatchEvent(new CustomEvent('adminToggled', { detail: { isAdmin: next } })); } catch (e) { }
        showToast(next ? 'Modo ADM ativado' : 'Modo ADM desativado', 'info');
    };

    useEffect(() => { try { (window as any).__isAdmin = isAdmin; } catch (e) { } }, []);

    const fetchMyItems = async () => {
        if (!user?.id) return setMyItems([]);
        try {
            const resp = await api.get(`/item/owner/${user.id}`);
            const mapped = resp.data.map((it: any) => ({
                id: it.id?.toString(),
                name: it.nome_item,
                description: it.descricao,
                price: parseFloat(it.valor_unitario),
                image: 'https://picsum.photos/300/300?random=' + it.id,
                ratingAvg: null,
                ratingCount: 0,
                type: it.categoria_item?.nome || 'Geral',
                ownerId: it.proprietario?.id?.toString()
            }));
            setMyItems(mapped);
        } catch (err) {
            console.error('Erro ao buscar meus itens', err);
            setMyItems([]);
        }
    };

    const fetchSolicitacoes = async () => {
        if (!user?.id) return setSolicitacoes([]);
        try {
            const resp = await api.get(`/solicitacao/owner/${user.id}`);
            setSolicitacoes(resp.data);
        } catch (err) {
            console.error('Erro ao buscar solicitacoes', err);
            setSolicitacoes([]);
        }
    };

    useEffect(() => {
        fetchMyItems();
        fetchSolicitacoes();
    }, [user]);

    useEffect(() => {
        const onItemCreated = (_ev: any) => {
            fetchMyItems();
        };
        const onSolicitacaoCreated = (_ev: any) => {
            fetchSolicitacoes();
            showToast('Nova solicitação recebida', 'info');
        };

        window.addEventListener('itemCreated', onItemCreated as EventListener);
        window.addEventListener('solicitacaoCreated', onSolicitacaoCreated as EventListener);
        return () => {
            window.removeEventListener('itemCreated', onItemCreated as EventListener);
            window.removeEventListener('solicitacaoCreated', onSolicitacaoCreated as EventListener);
        };
    }, [user]);

    const handleApprove = async (id: number) => {
        try {
            const resp = await api.put(`/solicitacao/status?id=${id}&statusSolicitacao=APROVADO`);
            showToast('Solicitação aprovada', 'success');
            if (resp.data && resp.data.emprestimo) {
                try { window.dispatchEvent(new CustomEvent('rentalCreated', { detail: resp.data.emprestimo })); } catch (e) { }
            }
            fetchSolicitacoes();
        } catch (err) {
            console.error('Erro ao aprovar', err);
            showToast('Erro ao aprovar', 'error');
        }
    };

    return (
        <div className="flex flex-col h-full bg-gray-50">
            <div className="relative bg-black h-40 rounded-b-[40px] mb-16 shadow-lg">
                {/* Avatar */}
                <div className="absolute -bottom-12 left-1/2 -translate-x-1/2 w-28 h-28 bg-gray-50 rounded-full  flex items-center justify-center overflow-hidden shadow-md">
                    <div className="w-full h-full bg-gray-200 flex items-center justify-center">
                        <Icons.User className="w-12 h-12 text-gray-400" />
                    </div>
                </div>
            </div>

            <div className="px-6 text-center mb-6 animate-fade-in">
                <div className="flex items-center justify-center gap-1 mb-2">
                    {/* Dynamic Stars */}
                    {[1, 2, 3, 4, 5].map(i => {
                        const stars = user?.estrelas ? parseFloat(user.estrelas) : 5;
                        return (
                            <Icons.Star
                                key={i}
                                className={`w-5 h-5 ${i <= Math.round(stars) ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'}`}
                            />
                        );
                    })}
                </div>

                <div className="flex flex-col gap-3 text-left mt-6 animate-slide-up">
                    <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex justify-between items-center transition-transform hover:scale-[1.02] duration-200">
                        <span className="text-sm font-mono"><strong>Nome:</strong> {user?.nome || 'Visitante'}</span>
                    </div>
                    <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex justify-between items-center cursor-pointer hover:bg-gray-50 transition-all hover:scale-[1.02] duration-200">
                        <span className="text-sm font-mono"><strong>Email:</strong> {user?.email || '-'}</span>
                        <Icons.Edit className="w-4 h-4 text-gray-400" />
                    </div>
                    {user?.enderecos && user.enderecos.length > 0 && (
                        <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-100 flex justify-between items-center cursor-pointer hover:bg-gray-50 transition-all hover:scale-[1.02] duration-200">
                            <span className="text-sm font-mono"><strong>Endereço:</strong> {user.enderecos[0].cidade} - {user.enderecos[0].estado}</span>
                            <Icons.Edit className="w-4 h-4 text-gray-400" />
                        </div>
                    )}
                </div>

                <Button className="mt-6 !bg-black hover:scale-105 transition-transform duration-200" onClick={() => onNavigate('rental-list')}>Histórico de aluguéis</Button>
            </div>

            <div className="px-6 text-center mb-6 animate-fade-in">
                <Button className={`mt-4 ${isAdmin ? '!bg-red-600' : ''} hover:scale-105 transition-transform duration-200`} onClick={toggleAdmin}>{isAdmin ? 'Desativar ADM' : 'Ativar modo ADM'}</Button>
            </div>

            <div className="px-6 pb-24 animate-slide-up animate-delay-200">
                <h3 className="font-bold mb-4 text-lg">Seus itens</h3>
                <div className="flex flex-col gap-3">
                    {myItems === null ? (
                        <p className="text-gray-500 text-sm">Carregando seus itens...</p>
                    ) : myItems.length === 0 ? (
                        <p className="text-gray-500 text-sm">Você ainda não tem itens cadastrados.</p>
                    ) : (
                        myItems.map((it, idx) => (
                            <div key={it.id} style={{ animationDelay: `${idx * 100}ms` }} className="bg-white p-3 rounded-xl border border-gray-100 shadow-sm flex items-center justify-between transition-transform hover:scale-[1.02] duration-200 animate-slide-up hover:shadow-lg hover:border-teal-100">
                                <div className="flex items-center gap-3">
                                    <img src={it.image} alt={it.name} className="w-12 h-12 rounded-lg object-cover" />
                                    <div>
                                        <div className="font-bold text-sm">{it.name}</div>
                                        <div className="text-xs text-gray-500">R$ {it.price.toFixed(2)}</div>
                                    </div>
                                </div>
                                <button onClick={() => onEditItem && onEditItem(it)} className="text-sm text-gray-600 hover:text-black transition-colors">Editar</button>
                            </div>
                        ))
                    )}
                </div>
            </div>

            <div className="px-6 pb-24 animate-slide-up animate-delay-300">
                <h3 className="font-bold mb-4 text-lg">Solicitações recebidas</h3>
                <div className="flex flex-col gap-3">
                    {solicitacoes === null ? (
                        <p className="text-gray-500 text-sm">Carregando solicitações...</p>
                    ) : solicitacoes.filter(s => s.status === 'PENDENTE').length === 0 ? (
                        <p className="text-gray-500 text-sm">Nenhuma solicitação pendente.</p>
                    ) : (
                        solicitacoes.filter(s => s.status === 'PENDENTE').map((s, idx) => (
                            <div key={s.id} style={{ animationDelay: `${idx * 100}ms` }} className="bg-white p-3 rounded-xl border border-gray-100 shadow-sm flex items-center justify-between transition-transform hover:scale-[1.02] duration-200 animate-slide-up hover:shadow-lg hover:border-teal-100">
                                <div>
                                    <div className="font-bold text-sm">{s.item?.nome_item || 'Item'}</div>
                                    <div className="text-xs text-gray-500">De {s.usuario?.nome || 'Usuário'}</div>
                                </div>
                                <div className="flex items-center gap-2">
                                    <button onClick={() => handleApprove(s.id)} className="px-3 py-1 bg-teal-500 text-white rounded-md hover:bg-teal-600 transition-colors active:scale-95">Aprovar</button>
                                    <span className="text-xs text-gray-400">{s.status}</span>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export const ItemDetailScreen: React.FC<{ item: Item; onBack: () => void; user?: any }> = ({ item, onBack, user }) => {
    const [days, setDays] = useState<number>(1);
    const [loadingReq, setLoadingReq] = useState<boolean>(false);

    const total = item.price * days;

    const handleRequest = async () => {
        if (!user) { showToast('Você precisa estar logado para solicitar.', 'error'); return; }
        setLoadingReq(true);
        try {
            const start = new Date();
            const end = new Date(start.getTime() + days * 24 * 60 * 60 * 1000);
            const formatLocal = (d: Date) => d.toISOString().replace('Z', '');
            const payload: any = {
                usuario: { id: parseInt(user.id) },
                item: { id: parseInt(item.id) },
                data_inicio: formatLocal(start),
                data_fim: formatLocal(end)
            };
            const resp = await api.post('/solicitacao', payload);
            try { window.dispatchEvent(new CustomEvent('solicitacaoCreated', { detail: resp.data })); } catch (e) { }
            showToast('Solicitação enviada', 'success');
            onBack();
        } catch (err) {
            console.error('Erro ao criar solicitacao', err);
            showToast('Erro ao enviar solicitação', 'error');
        } finally { setLoadingReq(false); }
    };

    const [reviews, setReviews] = useState<any[]>([]);
    useEffect(() => {
        if (item?.id) {
            api.getItemReviews(item.id)
                .then(r => setReviews(r.data))
                .catch(err => console.error('Erro ao buscar avaliações', err));
        }
    }, [item]);

    return (
        <div className="bg-white min-h-screen flex flex-col relative animate-fade-in">
            <div className="relative h-80 bg-gray-100">
                <img src={item.image} alt={item.name} className="w-full h-full object-cover" />
                <div className="absolute top-0 left-0 right-0 p-4 pt-12 bg-gradient-to-b from-black/50 to-transparent">
                    <button onClick={onBack} className="bg-white/20 backdrop-blur-md p-2 rounded-full text-white hover:bg-white/30 transition-colors hover:scale-110 duration-200">
                        <Icons.Back />
                    </button>
                </div>
                <div className="absolute bottom-[-1px] left-0 w-full overflow-hidden leading-[0] h-12 md:h-16">
                    <div className="absolute bottom-0 w-[200%] h-full flex animate-wave" style={{ animationDuration: '8s' }}>
                        <svg className="w-1/2 h-full text-white fill-current" preserveAspectRatio="none" viewBox="0 0 1440 320" xmlns="http://www.w3.org/2000/svg">
                            <path d="M0,160 C320,300, 420,300, 720,160 C1000,20, 1100,20, 1440,160 V320 H0 Z"></path>
                        </svg>
                        <svg className="w-1/2 h-full text-white fill-current -ml-[1px]" preserveAspectRatio="none" viewBox="0 0 1440 320" xmlns="http://www.w3.org/2000/svg">
                            <path d="M0,160 C320,300, 420,300, 720,160 C1000,20, 1100,20, 1440,160 V320 H0 Z"></path>
                        </svg>
                    </div>
                </div>
            </div>

            <div className="px-6 pt-2 pb-24 flex-1 animate-slide-up">
                <div className="flex justify-between items-start mb-2">
                    <h1 className="text-2xl font-bold">{item.name}</h1>
                    <span className="text-xl font-bold">R$: {item.price.toFixed(2)}</span>
                </div>

                <div className="flex gap-1 mb-4">
                    {[1, 2, 3, 4].map(i => <Icons.Star key={i} className="w-4 h-4 fill-yellow-400 text-yellow-400" />)}
                    <span className="text-xs text-gray-500 ml-2">({reviews.length > 0 ? reviews.length : (item.ratingCount ?? 0)} avaliações)</span>
                </div>

                <p className="text-gray-600 mb-8 leading-relaxed">
                    {item.description || 'Sem descrição.'}
                </p>

                <div className="mb-8">
                    <h3 className="font-bold text-lg mb-4">Avaliações</h3>
                    {reviews.length === 0 ? (
                        <p className="text-gray-500 text-sm">Este item ainda não possui avaliações.</p>
                    ) : (
                        <div className="flex flex-col gap-4">
                            {reviews.map((rev: any, idx) => (
                                <div key={rev.id} style={{ animationDelay: `${idx * 100}ms` }} className="bg-gray-50 p-4 rounded-xl border border-gray-100 transition-transform hover:scale-[1.01] duration-200 animate-slide-up hover:shadow-md">
                                    <div className="flex justify-between items-center mb-2">
                                        <span className="font-bold text-sm">{rev.usuario?.nome || 'Usuário'}</span>
                                        <div className="flex gap-0.5">
                                            {[1, 2, 3, 4, 5].map(s => (
                                                <Icons.Star key={s} className={`w-3 h-3 ${s <= (rev.nota || 0) ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'}`} />
                                            ))}
                                        </div>
                                    </div>
                                    <p className="text-xs text-gray-600">{rev.comentario || 'Sem comentário.'}</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <div className="mt-auto">
                    <label className="text-sm font-bold text-gray-700 block mb-2">Por quanto tempo?</label>
                    <input type="number" min={1} value={days} onChange={(e) => setDays(Math.max(1, Number(e.target.value) || 1))} className="w-full mb-6 p-3 border border-gray-300 rounded-lg bg-white focus:ring-2 focus:ring-black transition-shadow" />

                    <Button onClick={handleRequest} loading={loadingReq} className="hover:scale-[1.03] transition-transform duration-200">
                        {loadingReq ? 'Enviando...' : `Solicitar por R$: ${total}`}
                    </Button>
                </div>
            </div>
        </div>
    );
};

export const RentalListScreen: React.FC<{ onBack: () => void, onRentalClick: (rental: Rental) => void, user?: any }> = ({ onBack, onRentalClick, user }) => {
    const [borrowed, setBorrowed] = useState<Rental[]>([]);
    const [lent, setLent] = useState<Rental[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchRentals = async () => {
            setLoading(true);
            try {
                const userId = user?.id ?? 1;
                const respBorrowed = await api.get(`/emprestimo/user/${userId}`);
                const respLent = await api.get(`/emprestimo/owner/${userId}`);

                const mapEmp = (e: any): Rental => {
                    const start = e.data_inicio || e.data_emprestimo;
                    const end = e.data_devolucao_prevista;
                    const days = (start && end) ? Math.max(1, Math.round((new Date(end).getTime() - new Date(start).getTime()) / 86400000)) : 1;
                    let totalPrice = 0;
                    if (e.pagamento && (e.pagamento.valor_pagamento || e.pagamento.valor)) {
                        totalPrice = parseFloat(e.pagamento.valor_pagamento ?? e.pagamento.valor ?? 0);
                    } else if (e.item && e.item.valor_unitario) {
                        totalPrice = parseFloat(e.item.valor_unitario) * days;
                    }

                    let statusLabel = 'Pendente';
                    let statusKey = 'pending';
                    const payStatus = e.pagamento?.statusPagamento || null;
                    if (payStatus) {
                        switch (payStatus) {
                            case 'PENDENTE':
                                statusLabel = 'Pendente'; statusKey = 'pending'; break;
                            case 'ENVIADO_AO_DESTINTARIO':
                            case 'AGUARDANDO_ENVIO':
                            case 'ENVIADO_AO_REMETENTE':
                                statusLabel = 'Enviado'; statusKey = 'sent'; break;
                            case 'RECEBIDO':
                                statusLabel = 'Recebido'; statusKey = 'received'; break;
                            case 'CONCLUIDO':
                                statusLabel = 'Concluído'; statusKey = 'concluded'; break;
                            case 'CANCELADO':
                                statusLabel = 'Cancelado'; statusKey = 'cancelled'; break;
                            default:
                                statusLabel = payStatus; statusKey = 'pending';
                        }
                    } else if (typeof e.estado_pagamento !== 'undefined' && e.estado_pagamento !== null) {
                        statusLabel = e.estado_pagamento === 0 ? 'Pendente' : 'Devolvido';
                        statusKey = e.estado_pagamento === 0 ? 'pending' : 'returned';
                    }

                    return {
                        id: e.id?.toString(),
                        itemId: e.item?.id?.toString(),
                        itemName: e.item?.nome_item || 'Item',
                        itemImage: 'https://picsum.photos/300/300?random=' + (e.item?.id || '1'),
                        status: statusKey,
                        startDate: start,
                        endDate: end,
                        totalPrice: totalPrice,
                        _statusLabel: statusLabel,
                        _raw: e
                    } as any;
                };

                setBorrowed(respBorrowed.data.map(mapEmp));
                setLent(respLent.data.map(mapEmp));
            } catch (err) {
                console.error('Erro ao buscar histórico', err);
                showToast('Erro ao buscar histórico', 'error');
            } finally {
                setLoading(false);
            }
        };

        fetchRentals();

        const onRentalCreated = (_ev: any) => {
            fetchRentals();
        };

        window.addEventListener('rentalCreated', onRentalCreated as EventListener);
        return () => {
            window.removeEventListener('rentalCreated', onRentalCreated as EventListener);
        };
    }, [user]);

    if (loading) return <div className="p-6">Carregando...</div>;

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col animate-fade-in">
            <div className="bg-black text-white px-6 pt-12 pb-6 sticky top-0 z-10 shadow-lg">
                <div className="flex items-center gap-4">
                    <button onClick={onBack} className="p-2 -ml-2 rounded-full hover:bg-white/10 transition-colors transform hover:scale-110"><Icons.Back /></button>
                    <h1 className="text-xl font-mono font-bold">Histórico de Aluguéis</h1>
                </div>
            </div>

            <div className="p-4 flex flex-col gap-6 overflow-y-auto pb-20 animate-slide-up">

                <div>
                    <h2 className="text-lg font-bold mb-3 px-1 text-gray-800 flex items-center gap-2">
                        <Icons.Box className="w-5 h-5 text-teal-500" />
                        Peguei Emprestado
                    </h2>
                    <div className="flex flex-col gap-3">
                        {borrowed.map((rental, idx) => (
                            <div key={rental.id} style={{ animationDelay: `${idx * 100}ms` }} onClick={() => onRentalClick && onRentalClick(rental)} className="bg-white p-4 rounded-xl border border-gray-100 shadow-sm flex items-center gap-4 active:scale-95 transition-transform cursor-pointer hover:shadow-md hover:scale-[1.02] animate-slide-up">
                                <img src={rental.itemImage} alt={rental.itemName} className="w-16 h-16 rounded-lg bg-gray-200 object-cover" />
                                <div className="flex-1">
                                    <div className="flex justify-between items-start mb-1">
                                        <h3 className="font-bold text-sm">{rental.itemName}</h3>
                                        {(() => {
                                            const key = rental.status;
                                            const label = (rental as any)._statusLabel || (key === 'returned' ? 'Devolvido' : 'Pendente');
                                            const cls = key === 'received' || key === 'sent' ? 'bg-teal-100 text-teal-700' : key === 'concluded' || key === 'returned' || key === 'cancelled' ? 'bg-gray-100 text-gray-500' : 'bg-yellow-100 text-yellow-700';
                                            return <span className={`text-[10px] px-2 py-0.5 rounded-full font-bold uppercase ${cls}`}>{label}</span>;
                                        })()}
                                    </div>
                                    <p className="text-xs text-gray-500 mb-2">De {new Date(rental.startDate).toLocaleDateString('pt-BR')} até {new Date(rental.endDate).toLocaleDateString('pt-BR')}</p>
                                    <div className="flex justify-between items-end border-t pt-2 border-dashed border-gray-200">
                                        <span className="text-xs text-gray-400">Total</span>
                                        <span className="font-bold text-sm">R$ {rental.totalPrice?.toFixed(2)}</span>
                                    </div>
                                </div>
                                <Icons.ArrowRight className="text-gray-300 w-5 h-5 transition-transform group-hover:translate-x-1" />
                            </div>
                        ))}
                    </div>
                </div>

                <div>
                    <h2 className="text-lg font-bold mb-3 px-1 text-gray-800 flex items-center gap-2">
                        <Icons.User className="w-5 h-5 text-purple-500" />
                        Emprestei
                    </h2>
                    <div className="flex flex-col gap-3">
                        {lent.map((rental, idx) => (
                            <div key={rental.id} style={{ animationDelay: `${idx * 100}ms` }} onClick={() => onRentalClick && onRentalClick(rental)} className="bg-white p-4 rounded-xl border border-gray-100 shadow-sm flex items-center gap-4 active:scale-95 transition-transform cursor-pointer hover:shadow-md hover:scale-[1.02] animate-slide-up">
                                <img src={rental.itemImage} alt={rental.itemName} className="w-16 h-16 rounded-lg bg-gray-200 object-cover" />
                                <div className="flex-1">
                                    <div className="flex justify-between items-start mb-1">
                                        <h3 className="font-bold text-sm">{rental.itemName}</h3>
                                        {(() => {
                                            const key = rental.status;
                                            const label = (rental as any)._statusLabel || (key === 'returned' ? 'Devolvido' : 'Pendente');
                                            const cls = key === 'received' || key === 'sent' ? 'bg-teal-100 text-teal-700' : key === 'concluded' || key === 'returned' || key === 'cancelled' ? 'bg-gray-100 text-gray-500' : 'bg-yellow-100 text-yellow-700';
                                            return <span className={`text-[10px] px-2 py-0.5 rounded-full font-bold uppercase ${cls}`}>{label}</span>;
                                        })()}
                                    </div>
                                    <p className="text-xs text-gray-500 mb-2">De {new Date(rental.startDate).toLocaleDateString('pt-BR')} até {new Date(rental.endDate).toLocaleDateString('pt-BR')}</p>
                                    <div className="flex justify-between items-end border-t pt-2 border-dashed border-gray-200">
                                        <span className="text-xs text-gray-400">Total</span>
                                        <span className="font-bold text-sm">R$ {rental.totalPrice?.toFixed(2)}</span>
                                    </div>
                                </div>
                                <Icons.ArrowRight className="text-gray-300 w-5 h-5 transition-transform group-hover:translate-x-1" />
                            </div>
                        ))}
                    </div>
                </div>

            </div>
        </div>
    )
}


const getTotalPrice = (rental: any) => {
    if (typeof rental.totalPrice === 'number') return rental.totalPrice;

    const start = rental.data_inicio || rental.data_emprestimo || rental.startDate;
    const end = rental.data_devolucao_prevista || rental.endDate;
    const days = (start && end) ? Math.max(1, Math.round((new Date(end).getTime() - new Date(start).getTime()) / 86400000)) : 1;

    const raw = rental._raw || rental;
    if (raw.pagamento && (raw.pagamento.valor_pagamento || raw.pagamento.valor)) {
        return parseFloat(raw.pagamento.valor_pagamento ?? raw.pagamento.valor ?? 0);
    } else if (raw.item && raw.item.valor_unitario) {
        return parseFloat(raw.item.valor_unitario) * days;
    }

    return 0;
};

export const RentalStatusScreen: React.FC<{ onBack: () => void, rental?: Rental, user?: any }> = ({ onBack, rental, user }) => {

    const [current, setCurrent] = useState<any>(rental || FALLBACK_RENTAL);
    const [showReview, setShowReview] = useState(false);
    const [updating, setUpdating] = useState(false);
    const [selectedStatus, setSelectedStatus] = useState<string>('PENDENTE');


    const statusToStep = (status?: string, raw?: any) => {
        if (!status && raw) status = raw?.pagamento?.statusPagamento;
        if (!status && raw && typeof raw.estado_pagamento !== 'undefined') {

            return raw.estado_pagamento === 0 ? 0 : 3;
        }
        switch (status) {
            case 'PENDENTE': return 0;
            case 'AGUARDANDO_ENVIO':
            case 'ENVIADO_AO_DESTINTARIO':
            case 'ENVIADO_AO_REMETENTE': return 1;
            case 'RECEBIDO': return 2;
            case 'CONCLUIDO': return 3;
            case 'CANCELADO': return 3;
            default: return 0;
        }
    };


    const statusLabel = (raw?: any) => {
        const pay = raw?.pagamento?.statusPagamento;
        if (pay) return pay;
        if (typeof raw?.estado_pagamento !== 'undefined') return raw.estado_pagamento === 0 ? 'Pendente' : 'Devolvido';
        return 'Pendente';
    };


    useEffect(() => {
        const r = rental || FALLBACK_RENTAL;
        setCurrent(r);
        const raw = (r as any)._raw || r;
        const payStatus = raw?.pagamento?.statusPagamento;
        if (payStatus) setSelectedStatus(payStatus);
        else if (typeof raw?.estado_pagamento !== 'undefined') setSelectedStatus(raw.estado_pagamento === 0 ? 'PENDENTE' : 'CONCLUIDO');
        else setSelectedStatus('PENDENTE');

        (async () => {
            try {
                const hasRaw = !!((r as any)._raw);
                if (!hasRaw && r?.id) {
                    const resp = await api.get(`/emprestimo/${r.id}`);
                    const e = resp.data;
                    const newRental = {
                        id: e.id?.toString(),
                        itemId: e.item?.id?.toString(),
                        itemName: e.item?.nome_item || 'Item',
                        itemImage: 'https://picsum.photos/300/300?random=' + (e.item?.id || '1'),
                        status: 'pending',
                        startDate: e.data_inicio || e.data_emprestimo,
                        endDate: e.data_devolucao_prevista,
                        totalPrice: e.pagamento?.valor_pagamento ?? e.pagamento?.valor ?? (e.item?.valor_unitario || 0),
                        _statusLabel: e.pagamento?.statusPagamento || (e.estado_pagamento === 0 ? 'Pendente' : 'Devolvido'),
                        _raw: e
                    } as any;
                    setCurrent(newRental);
                    const pay = e.pagamento?.statusPagamento;
                    if (pay) setSelectedStatus(pay);
                }
            } catch (err) {
                console.warn('Não foi possível buscar emprestimo completo', err);
            }
        })();
    }, [rental]);


    const [rating, setRating] = useState<number>(0);
    const [comment, setComment] = useState<string>('');

    const [rateBorrower, setRateBorrower] = useState(false);
    const [borrowerRating, setBorrowerRating] = useState(0);

    const confirmReceived = async () => {
        if (!current?.id) return;
        try {
            setUpdating(true);
            await api.put(`/emprestimo/status?id=${current.id}&status=RECEBIDO`);
            showToast('Status alterado para RECEBIDO', 'success');

            const resp = await api.get(`/emprestimo/${current.id}`);
            const e = resp.data;
            const newRental = { id: e.id?.toString(), itemId: e.item?.id?.toString(), itemName: e.item?.nome_item, itemImage: 'https://picsum.photos/300/300?random=' + (e.item?.id || '1'), startDate: e.data_inicio || e.data_emprestimo, endDate: e.data_devolucao_prevista, totalPrice: e.pagamento?.valor_pagamento ?? e.pagamento?.valor ?? (e.item?.valor_unitario || 0), _statusLabel: e.pagamento?.statusPagamento, _raw: e } as any;
            setCurrent(newRental);
            setSelectedStatus(e.pagamento?.statusPagamento ?? 'RECEBIDO');
        } catch (err) {
            console.error('Erro ao confirmar recebimento', err);
            showToast('Erro ao confirmar recebimento', 'error');
        } finally { setUpdating(false); }
    };

    const sendRating = async () => {
        if (!current?.id || !user) { showToast('Você precisa estar logado para avaliar', 'error'); return; }
        try {
            const payload = {
                usuario: { id: parseInt(user.id) },
                emprestimo: { id: parseInt(current.id) },
                nota: rating,
                comentario: comment
            };
            await api.post('/avaliacao', payload);
            showToast('Avaliação enviada', 'success');
            setRating(0); setComment('');
        } catch (err) {
            console.error('Erro ao enviar avaliacao', err);
            showToast('Erro ao enviar avaliação', 'error');
        }
    };

    const handleRateBorrower = async () => {
        const raw = (current as any)._raw || (current as any);
        const borrowerId = raw?.solicitacaoEmprestimo?.usuario?.id || raw?.usuario?.id;
        if (!borrowerId) { showToast('Erro ao identificar locatário', 'error'); return; }
        try {
            await api.rateUser(borrowerId, borrowerRating);
            showToast('Avaliação enviada com sucesso', 'success');
            setRateBorrower(false);
        } catch (err) {
            console.error('Erro ao avaliar locatário', err);
            showToast('Erro ao avaliar locatário', 'error');
        }
    };

    const [adminEnabled, setAdminEnabled] = useState<boolean>(!!(window as any).__isAdmin);
    useEffect(() => {
        const handler = (ev: any) => setAdminEnabled(!!ev?.detail?.isAdmin);
        window.addEventListener('adminToggled', handler as EventListener);
        return () => window.removeEventListener('adminToggled', handler as EventListener);
    }, []);

    const handleAdminChange = async (newStatus: string) => {
        if (!current?.id) return;
        try {
            setUpdating(true);
            await api.put(`/emprestimo/status?id=${current.id}&status=${newStatus}`);
            showToast(`Status atualizado para ${newStatus}`, 'success');
            setSelectedStatus(newStatus);

            const resp = await api.get(`/emprestimo/${current.id}`);
            if (resp.data && resp.data.pagamento) {

            }
        } catch (err) {
            console.error('Erro ao atualizar status', err);
            showToast('Erro ao atualizar status', 'error');
        } finally {
            setUpdating(false);
        }
    };


    const step = statusToStep(selectedStatus, (current as any)._raw || (current as any));

    const raw = (current as any)._raw || (current as any);
    const itemName = raw?.item?.nome_item || (current as any).itemName || 'Item';
    const borrowerName = raw?.solicitacaoEmprestimo?.usuario?.nome || raw?.destinatario?.nome || raw?.usuario?.nome || (raw?.remetente?.nome ? raw.remetente.nome : undefined) || '-';
    const ownerName = raw?.item?.proprietario?.nome || raw?.remetente?.nome || raw?.item?.proprietario?.nome || '-';


    const ownerId = raw?.item?.proprietario?.id?.toString() || (current as any).ownerId;
    const isOwner = user?.id && ownerId && user.id.toString() === ownerId.toString();

    const displayLabel = statusLabel(raw);
    const totalPrice = getTotalPrice(current);

    return (
        <div className="min-h-screen bg-white flex flex-col animate-fade-in">
            <div className="relative h-64 overflow-hidden">
                <div className="absolute inset-0 bg-black">
                    <img src={current.itemImage} alt={current.itemName || 'Item image'} className="w-full h-full object-cover opacity-50 transition-transform duration-700 hover:scale-105" />
                </div>
                <div className="absolute inset-0 flex flex-col items-center justify-center text-white z-10 pt-6 animate-scale-in">
                    <h1 className="text-3xl font-mono font-bold">Status do aluguel</h1>
                    <p className="opacity-80">ID: {String(current.id).toUpperCase()}</p>
                    <p className="mt-2 font-bold">{displayLabel}</p>
                    <div className="mt-3 text-sm text-white/90">
                        <div>{itemName}</div>
                        <div className="text-xs opacity-90">Locador: {ownerName} • Locatário: {borrowerName}</div>
                    </div>
                </div>
                <button onClick={onBack} className="absolute top-12 left-6 text-white z-20 p-2 bg-white/10 backdrop-blur-sm rounded-full hover:bg-white/20 transition-all hover:scale-110"><Icons.Back /></button>

                <div className="absolute bottom-[-1px] left-0 w-full overflow-hidden leading-[0]">
                    <svg className="block w-full h-12 text-white fill-current" preserveAspectRatio="none" viewBox="0 0 1440 320" xmlns="http://www.w3.org/2000/svg">
                        <path d="M0,224L48,213.3C96,203,192,181,288,181.3C384,181,480,203,576,224C672,245,768,267,864,261.3C960,256,1056,224,1152,202.7C1248,181,1344,171,1392,165.3L1440,160L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path>
                    </svg>
                </div>
            </div>

            <div className="flex-1 bg-white px-6 py-8 relative z-20 animate-slide-up">
                <div className="flex justify-between text-xs font-mono mb-8 text-gray-500">
                    <span>Data do pedido: <strong className="text-black">{new Date((current as any).startDate).toLocaleDateString('pt-BR')}</strong></span>
                    <span className="text-teal-600">Devolução: {new Date((current as any).endDate).toLocaleDateString('pt-BR')}</span>
                </div>

                <div className="relative flex flex-col gap-4 mb-6">
                    <div className="relative flex justify-between items-center mb-2 ">
                        <div className="absolute top-[7px] left-0 right-0 h-0.5 bg-gray-200 -z-10 "></div>
                        <div className=" absolute top-[7px] left-0 h-0.5 bg-teal-400 -z-10 transition-all duration-500" style={{ width: `${step * 33}%` }}></div>

                        {['Pedido', 'Enviado', 'Chegou', 'Concluído'].map((label, idx) => (
                            <div key={idx} className="flex flex-col items-center gap-2">
                                <div className={`w-4 h-4 rounded-full border-2 transition-all duration-300 ${idx <= step ? 'bg-teal-400 border-teal-400 scale-110' : 'bg-white border-gray-300'}`}></div>
                                <span className={`text-[10px] text-center max-w-[50px] transition-colors duration-300 ${idx <= step ? 'text-black font-bold' : 'text-gray-400'}`}>{label}</span>
                            </div>
                        ))}
                    </div>
                </div>

                {adminEnabled && (
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-2">Alterar status (ADM)</label>
                        <select disabled={updating} value={selectedStatus} onChange={(e) => handleAdminChange(e.target.value)} className="w-full p-3 border rounded-lg">
                            <option value="PENDENTE">PENDENTE</option>
                            <option value="AGUARDANDO_ENVIO">AGUARDANDO_ENVIO</option>
                            <option value="ENVIADO_AO_DESTINTARIO">ENVIADO_AO_DESTINTARIO</option>
                            <option value="ENVIADO_AO_REMETENTE">ENVIADO_AO_REMETENTE</option>
                            <option value="RECEBIDO">RECEBIDO</option>
                            <option value="CONCLUIDO">CONCLUIDO</option>
                            <option value="CANCELADO">CANCELADO</option>
                        </select>
                    </div>
                )}

                {/* Only show receipt confirmation if NOT owner */}
                {!isOwner && (
                    <div className="text-center mb-8">
                        <p className="font-bold text-lg">Seu pedido já chegou?</p>
                        <div className="flex items-center justify-center gap-3">
                            <button onClick={() => setShowReview(true)} className="text-teal-500 text-sm underline hover:text-teal-700 transition-colors">Avaliar / Confirmar</button>
                            <button onClick={confirmReceived} disabled={updating} className="px-3 py-1 bg-teal-500 text-white rounded-md hover:bg-teal-600 transition-colors transform active:scale-95">{updating ? 'Aguarde...' : 'Confirmar recebimento'}</button>
                        </div>
                    </div>
                )}

                {/* Owner Rating Borrower Section (Only if Owner and Concluded) */}
                {isOwner && (selectedStatus === 'CONCLUIDO' || selectedStatus === 'DEVOLVIDO') && (
                    <div className="bg-gray-50 p-4 rounded-xl border border-gray-100 mb-6 text-center animate-fade-in">
                        <h4 className="font-bold mb-2">Como foi o locatário?</h4>
                        {!rateBorrower ? (
                            <Button onClick={() => setRateBorrower(true)} className="!bg-black !text-white !py-2 !text-xs hover:scale-105 transition-transform">Avaliar Locatário</Button>
                        ) : (
                            <div className="animate-fade-in">
                                <div className="flex justify-center gap-2 mb-3">
                                    {[1, 2, 3, 4, 5].map(s => (
                                        <button key={s} onClick={() => setBorrowerRating(s)} className={`p-1 transition-transform hover:scale-125 ${borrowerRating >= s ? 'text-yellow-400' : 'text-gray-300'}`}>
                                            <Icons.Star className="w-6 h-6" />
                                        </button>
                                    ))}
                                </div>
                                <div className="flex gap-2 justify-center">
                                    <button onClick={handleRateBorrower} className="px-3 py-1 bg-green-600 text-white rounded text-xs hover:bg-green-700 transition-colors">Enviar</button>
                                    <button onClick={() => setRateBorrower(false)} className="px-3 py-1 bg-gray-300 text-black rounded text-xs hover:bg-gray-400 transition-colors">Cancelar</button>
                                </div>
                            </div>
                        )}
                    </div>
                )}

                <div className="border-t pt-6 grid grid-cols-2 gap-4 text-xs text-gray-600">
                    <div>
                        <h4 className="font-bold text-black mb-1">Pagamento</h4>
                        <p>Visa **24</p>
                    </div>
                    <div>
                        <h4 className="font-bold text-black mb-1">Entrega</h4>
                        {(() => {
                            const add = raw?.item?.proprietario?.enderecos?.[0] || (current as any).address || raw?.remetente?.enderecos?.[0];
                            if (add) {
                                return (
                                    <>
                                        <p>{add.rua}, {add.numero}</p>
                                        <p>{add.cidade} - {add.estado}</p>
                                    </>
                                );
                            }
                            return <p className="text-gray-400 italic">Endereço não disponível</p>;
                        })()}
                    </div>
                </div>

                <div className="border-t mt-6 pt-6 text-xs text-gray-600">
                    <h4 className="font-bold text-black mb-2">Resumo do pedido</h4>
                    <div className="flex justify-between mb-1"><span>Preço/dia</span><span>R$ {(getTotalPrice(current) / (Math.max(1, Math.round((new Date((current as any).endDate).getTime() - new Date((current as any).startDate).getTime()) / 86400000)))).toFixed(2)}</span></div>
                    <div className="flex justify-between mb-1"><span>Prazo</span><span>{Math.max(1, Math.round((new Date((current as any).endDate).getTime() - new Date((current as any).startDate).getTime()) / 86400000))} dias</span></div>
                    <div className="flex justify-between mb-1"><span>Taxa</span><span>R$ 0,00</span></div>
                    <div className="flex justify-between font-bold text-black text-sm mt-2"><span>Total</span><span>R$ {totalPrice.toFixed(2)}</span></div>
                </div>
            </div>

            {showReview && (
                <div className="fixed inset-0 bg-black/60 z-50 flex items-center justify-center p-6 animate-fade-in">
                    <div className="bg-white rounded-2xl p-6 w-full max-w-sm text-center relative shadow-2xl">
                        <div className="w-16 h-16 bg-gray-100 rounded-full mx-auto -mt-12 mb-4 overflow-hidden border-4 border-white shadow-lg">
                            <img src={(current as any).itemImage} alt={(current as any).itemName || 'Item image'} className="w-full h-full object-cover" />
                        </div>
                        <h3 className="font-bold text-lg">Avalie a sua experiência</h3>
                        <div className="flex justify-center gap-2 my-4">
                            {[1, 2, 3, 4, 5].map(s => (
                                <button key={s} onClick={() => setRating(s)} className={`p-1 ${rating >= s ? 'text-yellow-400' : 'text-gray-300'}`}><Icons.Star className="w-8 h-8" /></button>
                            ))}
                        </div>
                        <textarea value={comment} onChange={(e) => setComment(e.target.value)} placeholder="Qual o motivo da sua avaliação?" className="w-full p-3 rounded-md border !bg-gray-100 !text-sm mb-3" />
                        <div className="flex gap-2 justify-center">
                            <Button onClick={sendRating}>Enviar avaliação</Button>
                            <Button onClick={() => setShowReview(false)} className="!bg-gray-200 !text-black">Cancelar</Button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};


import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

const extendedApi = {
    ...api,
    get: api.get,
    post: api.post,
    put: api.put,
    patch: api.patch,
    delete: api.delete,

    rateUser: (userId: string | number, rate: number) =>
        api.patch(`/user/${userId}/rate?rate=${rate}`),

    getItemReviews: (itemId: string | number) =>
        api.get(`/avaliacao/item/${itemId}`),
};

export default extendedApi;

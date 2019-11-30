import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionDiscount, defaultValue } from 'app/shared/model/subscription-discount.model';

export const ACTION_TYPES = {
  SEARCH_SUBSCRIPTIONDISCOUNTS: 'subscriptionDiscount/SEARCH_SUBSCRIPTIONDISCOUNTS',
  FETCH_SUBSCRIPTIONDISCOUNT_LIST: 'subscriptionDiscount/FETCH_SUBSCRIPTIONDISCOUNT_LIST',
  FETCH_SUBSCRIPTIONDISCOUNT: 'subscriptionDiscount/FETCH_SUBSCRIPTIONDISCOUNT',
  CREATE_SUBSCRIPTIONDISCOUNT: 'subscriptionDiscount/CREATE_SUBSCRIPTIONDISCOUNT',
  UPDATE_SUBSCRIPTIONDISCOUNT: 'subscriptionDiscount/UPDATE_SUBSCRIPTIONDISCOUNT',
  DELETE_SUBSCRIPTIONDISCOUNT: 'subscriptionDiscount/DELETE_SUBSCRIPTIONDISCOUNT',
  RESET: 'subscriptionDiscount/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionDiscount>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionDiscountState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionDiscountState = initialState, action): SubscriptionDiscountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SUBSCRIPTIONDISCOUNTS):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONDISCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONDISCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONDISCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SUBSCRIPTIONDISCOUNTS):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONDISCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONDISCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONDISCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SUBSCRIPTIONDISCOUNTS):
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONDISCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONDISCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONDISCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/subscription-discounts';
const apiSearchUrl = 'api/_search/subscription-discounts';

// Actions

export const getSearchEntities: ICrudSearchAction<ISubscriptionDiscount> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SUBSCRIPTIONDISCOUNTS,
  payload: axios.get<ISubscriptionDiscount>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<ISubscriptionDiscount> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT_LIST,
  payload: axios.get<ISubscriptionDiscount>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionDiscount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONDISCOUNT,
    payload: axios.get<ISubscriptionDiscount>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONDISCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionDiscount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONDISCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionDiscount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONDISCOUNT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

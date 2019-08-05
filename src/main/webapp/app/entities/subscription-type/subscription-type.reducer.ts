import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISubscriptionType, defaultValue } from 'app/shared/model/subscription-type.model';

export const ACTION_TYPES = {
  SEARCH_SUBSCRIPTIONTYPES: 'subscriptionType/SEARCH_SUBSCRIPTIONTYPES',
  FETCH_SUBSCRIPTIONTYPE_LIST: 'subscriptionType/FETCH_SUBSCRIPTIONTYPE_LIST',
  FETCH_SUBSCRIPTIONTYPE: 'subscriptionType/FETCH_SUBSCRIPTIONTYPE',
  CREATE_SUBSCRIPTIONTYPE: 'subscriptionType/CREATE_SUBSCRIPTIONTYPE',
  UPDATE_SUBSCRIPTIONTYPE: 'subscriptionType/UPDATE_SUBSCRIPTIONTYPE',
  DELETE_SUBSCRIPTIONTYPE: 'subscriptionType/DELETE_SUBSCRIPTIONTYPE',
  RESET: 'subscriptionType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISubscriptionType>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SubscriptionTypeState = Readonly<typeof initialState>;

// Reducer

export default (state: SubscriptionTypeState = initialState, action): SubscriptionTypeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SUBSCRIPTIONTYPES):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SUBSCRIPTIONTYPE):
    case REQUEST(ACTION_TYPES.UPDATE_SUBSCRIPTIONTYPE):
    case REQUEST(ACTION_TYPES.DELETE_SUBSCRIPTIONTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SUBSCRIPTIONTYPES):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE):
    case FAILURE(ACTION_TYPES.CREATE_SUBSCRIPTIONTYPE):
    case FAILURE(ACTION_TYPES.UPDATE_SUBSCRIPTIONTYPE):
    case FAILURE(ACTION_TYPES.DELETE_SUBSCRIPTIONTYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SUBSCRIPTIONTYPES):
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SUBSCRIPTIONTYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_SUBSCRIPTIONTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SUBSCRIPTIONTYPE):
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

const apiUrl = 'api/subscription-types';
const apiSearchUrl = 'api/_search/subscription-types';

// Actions

export const getSearchEntities: ICrudSearchAction<ISubscriptionType> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SUBSCRIPTIONTYPES,
  payload: axios.get<ISubscriptionType>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<ISubscriptionType> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE_LIST,
  payload: axios.get<ISubscriptionType>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISubscriptionType> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SUBSCRIPTIONTYPE,
    payload: axios.get<ISubscriptionType>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISubscriptionType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SUBSCRIPTIONTYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISubscriptionType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SUBSCRIPTIONTYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISubscriptionType> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SUBSCRIPTIONTYPE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

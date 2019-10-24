import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRace, defaultValue } from 'app/shared/model/race.model';

export const ACTION_TYPES = {
  SEARCH_RACES: 'race/SEARCH_RACES',
  FETCH_NEXT_RACE_LIST: 'race/FETCH_NEXT_RACE_LIST',
  FETCH_NEXT_RACE_BY_TEAM_LIST: 'race/FETCH_NEXT_RACE_BY_TEAM_LIST',
  FETCH_RACE: 'race/FETCH_RACE',
  CREATE_RACE: 'race/CREATE_RACE',
  UPDATE_RACE: 'race/UPDATE_RACE',
  DELETE_RACE: 'race/DELETE_RACE',
  SET_BLOB: 'race/SET_BLOB',
  ADD_SUBSCRIPTION_TYPE: 'race/ADD_SUBSCRIPTION_TYPE',
  REMOVE_SUBSCRIPTION_TYPE: 'race/REMOVE_SUBSCRIPTION_TYPE',
  ADD_PATH_TYPE: 'race/ADD_SUBSCRIPTION',
  REMOVE_PATH_TYPE: 'race/REMOVE_SUBSCRIPTION',
  RESET: 'race/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRace>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RaceState = Readonly<typeof initialState>;

// Reducer

export default (state: RaceState = initialState, action): RaceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RACES):
    case REQUEST(ACTION_TYPES.FETCH_NEXT_RACE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_NEXT_RACE_BY_TEAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RACE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RACE):
    case REQUEST(ACTION_TYPES.UPDATE_RACE):
    case REQUEST(ACTION_TYPES.DELETE_RACE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RACES):
    case FAILURE(ACTION_TYPES.FETCH_NEXT_RACE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_NEXT_RACE_BY_TEAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RACE):
    case FAILURE(ACTION_TYPES.CREATE_RACE):
    case FAILURE(ACTION_TYPES.UPDATE_RACE):
    case FAILURE(ACTION_TYPES.DELETE_RACE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RACES):
    case SUCCESS(ACTION_TYPES.FETCH_NEXT_RACE_LIST):
    case SUCCESS(ACTION_TYPES.FETCH_NEXT_RACE_BY_TEAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RACE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RACE):
    case SUCCESS(ACTION_TYPES.UPDATE_RACE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RACE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name + 'Image']: data,
          [name + 'ContentType']: contentType,
          [name + 'FileName']: name
        }
      };
    case ACTION_TYPES.ADD_SUBSCRIPTION_TYPE:
      const { subscriptionType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          subscriptionTypes: [...state.entity.subscriptionTypes, subscriptionType]
        }
      };
    case ACTION_TYPES.REMOVE_SUBSCRIPTION_TYPE:
      const removingSubscriptionTypes = [...state.entity.subscriptionTypes];
      removingSubscriptionTypes.splice(action.payload.index, 1);
      return {
        ...state,
        entity: {
          ...state.entity,
          subscriptionTypes: [...removingSubscriptionTypes]
        }
      };
    case ACTION_TYPES.ADD_PATH_TYPE:
      const { pathType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          pathTypes: [...state.entity.pathTypes, pathType]
        }
      };
    case ACTION_TYPES.REMOVE_PATH_TYPE:
      const removingPathTypes = [...state.entity.pathTypes];
      removingPathTypes.splice(action.payload.index, 1);

      const result = {
        ...state,
        entity: {
          ...state.entity,
          pathTypes: [...removingPathTypes]
        }
      };
      return result;

    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/races';
const apiSearchUrl = 'api/_search/races';

// Actions

export const getSearchEntities: ICrudSearchAction<IRace> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RACES,
  payload: axios.get<IRace>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IRace> = (page, size, sort) => {
  const requestUrl = `${apiUrl}/next${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_NEXT_RACE_LIST,
    payload: axios.get<IRace>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntitiesByTeam: ICrudGetAllAction<IRace> = (page, size, sort) => {
  const requestUrl = `${apiUrl}/next/team${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_NEXT_RACE_BY_TEAM_LIST,
    payload: axios.get<IRace>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRace> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RACE,
    payload: axios.get<IRace>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRace> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RACE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntitiesByTeam());
  return result;
};

export const updateEntity: ICrudPutAction<IRace> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RACE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRace> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RACE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const addSubscriptionType = subscriptionType => ({
  type: ACTION_TYPES.ADD_SUBSCRIPTION_TYPE,
  payload: {
    subscriptionType
  }
});

export const removeSubscriptionType = index => ({
  type: ACTION_TYPES.REMOVE_SUBSCRIPTION_TYPE,
  payload: {
    index
  }
});

export const addPathType = pathType => ({
  type: ACTION_TYPES.ADD_PATH_TYPE,
  payload: {
    pathType
  }
});

export const removePathType = index => ({
  type: ACTION_TYPES.REMOVE_PATH_TYPE,
  payload: {
    index
  }
});

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
